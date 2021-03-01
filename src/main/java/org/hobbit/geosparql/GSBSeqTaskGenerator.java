package org.hobbit.geosparql;

import java.io.ByteArrayOutputStream;
import java.io.File;
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
        answers = new String[GSBConstants.GSB_ANSWERS.length];
        
    	// reading query answers
        try {
            for (int i=0; i < GSBConstants.GSB_ANSWERS.length; i++) {
                InputStream inputStream = new FileInputStream("gsb_answers/" + GSBConstants.GSB_ANSWERS[i]);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                ResultSetFactory rsf = new ResultSetFactory();                
                ResultSetFormatter.outputAsJSON(outputStream, rsf.fromXML(inputStream));
                answers[i] = outputStream.toString();
                inputStream.close();
                for (int k=1; ; k++) {
                    String alternativeAnswerFileName = GSBConstants.GSB_ANSWERS[i].replace(".srx","") + "-alternative-" + k + ".srx";
                    LOGGER.info("Looking for an alternative file called: " + alternativeAnswerFileName);
                    File alternativeAnswerFile = new File("gsb_answers/" + alternativeAnswerFileName);
                    if (!alternativeAnswerFile.exists()) break;
                    else {
                        LOGGER.info("Alternative file found: " + alternativeAnswerFileName);
                        InputStream alternativeAnswerInputStream = new FileInputStream(alternativeAnswerFile);
                        ByteArrayOutputStream alternativeAnswerOutputStream = new ByteArrayOutputStream();
                        ResultSetFormatter.outputAsJSON(alternativeAnswerOutputStream, rsf.fromXML(alternativeAnswerInputStream));
                        answers[i] = answers[i] + "======" + alternativeAnswerOutputStream.toString(); // add the detected alternative expected answer with a corresponding delimiter
                        alternativeAnswerInputStream.close();
                        LOGGER.info("answers[" + i + "]: " + answers[i]);
                    }
                }
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
        String answerIndexString = parts[0].trim().substring(3);
        int answerIndex = Integer.parseInt(answerIndexString) - 1; // The first line is a comment denoting the query (#Q-1, #Q-2, ...)
        String ans = answers[answerIndex];
        data = RabbitMQUtils.writeString("#Q-" + (answerIndex+1) + "\n\n" + ans);
        sendTaskToEvalStorage(taskIdString, timestamp, data);
    }
}
