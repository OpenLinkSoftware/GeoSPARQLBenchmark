package org.hobbit.geosparql;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import org.apache.jena.query.ResultSetFactory;
import org.apache.jena.query.ResultSetFormatter;
import org.hobbit.core.components.AbstractSequencingTaskGenerator;
import org.hobbit.core.rabbit.RabbitMQUtils;
import org.hobbit.geosparql.util.GSBConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GSBSeqTaskGenerator extends AbstractSequencingTaskGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(GSBSeqTaskGenerator.class);

    private boolean finished = false;
      
    String [][] params;
    String [] answers;
    Random [] rndms;
    int [] frequency;
    
    long numberOfUpdates = 0;
    int selectId = 0;
	
    public GSBSeqTaskGenerator() {
    	super();
    }
    
    @Override
    public void init() throws Exception {
        LOGGER.info("Initialization begins.");
        super.init();
        
        internalInit();
        LOGGER.info("Initialization is over.");
    }
    
    private void internalInit() {        
    	// reading query answers
        try {
            for (int i=0; i < GSBConstants.GSB_ANSWERS.length; i++) {
                InputStream inputStream = new FileInputStream("gsb_answers/" + GSBConstants.GSB_ANSWERS[i]);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                ResultSetFactory rsf = new ResultSetFactory();                
                ResultSetFormatter.outputAsJSON(outputStream, rsf.fromXML(inputStream));
                answers[i] = outputStream.toString();
                inputStream.close();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void close() throws IOException {
        // Free the resources you requested here

        // Always close the super class after yours!
        super.close();
    }

    @Override
    protected void generateTask(byte[] data) throws Exception {
        if (finished)
            return;
        
        String taskIdString = getNextTaskId();
        long timestamp = System.currentTimeMillis();

        // Get the query from the Data Generator
        // and send it to the System Adapter for execution
        String dataString = RabbitMQUtils.readString(data);
        byte[] task = RabbitMQUtils.writeByteArrays(new byte[][] { RabbitMQUtils.writeString(dataString) });        
        sendTaskToSystemAdapter(taskIdString, task);

        // Locate the corresponding query answer
        // and send it to the Evaluation Store for evaluation
        String [] parts = dataString.split("\n");
        int answerIndex = ((int)parts[0].charAt(parts[0].length() - 1)) - 1; // The first line is a comment denoting the query (#Q01, #Q02, ...)
        String ans = answers[answerIndex];
        data = RabbitMQUtils.writeString("#Q0" + (answerIndex+1) + "\n\n" + ans);
        sendTaskToEvalStorage(taskIdString, timestamp, data);
    }
}
