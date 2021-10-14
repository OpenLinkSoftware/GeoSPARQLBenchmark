package org.hobbit.geosparql;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.concurrent.Semaphore;

import org.apache.commons.io.IOUtils;
import org.hobbit.core.components.AbstractDataGenerator;
import org.hobbit.core.rabbit.RabbitMQUtils;
import org.hobbit.geosparql.util.GSBConstants;
import org.hobbit.geosparql.util.VirtuosoSystemAdapterConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GSBDataGenerator extends AbstractDataGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(GSBDataGenerator.class);
    private Semaphore generateTasks = new Semaphore(0);
	
    public GSBDataGenerator() {
    	
    }
    
    @Override
    public void init() throws Exception {
    	LOGGER.info("Initialization begins.");
        // Always init the super class first!
        super.init();

	// Your initialization code comes here...
        internalInit();
        LOGGER.info("Initialization is over.");
    }
    
    private void internalInit() {
        Map<String, String> env = System.getenv();
    }

    @Override
    protected void generateData() throws Exception {
        LOGGER.info("Data Generator is running...");

        getFileAndSendData();

        generateTasks.acquire();
    }
	
    @Override
	public void close() throws IOException {
        // Free the resources you requested here
    	
        // Always close the super class after yours!
        super.close();
    }
    
    private void getFileAndSendData() {
    	// TODO  
        String datasetFile = GSBConstants.GSB_PATH+"/gsb_dataset/dataset.rdf";
        String datasetURI = "http://openlinksw.com/geosparql/dataset.rdf";
        
        try {
            LOGGER.info("Getting file " + datasetFile);           
            InputStream inputStream = new FileInputStream(datasetFile);

            String graphUri = datasetURI;
            byte[] data = IOUtils.toByteArray(inputStream);
            byte[] dataForSending = RabbitMQUtils.writeByteArrays(null, new byte[][]{RabbitMQUtils.writeString(graphUri)}, data);
            sendDataToSystemAdapter(dataForSending);
            inputStream.close();

            LOGGER.info("File " + datasetFile + " has been downloaded successfully and sent.");

            ByteBuffer buffer = ByteBuffer.allocate(5);
            buffer.putInt(1); // buffer.putInt(files.length);
            buffer.put((byte) 1);
            sendToCmdQueue(VirtuosoSystemAdapterConstants.BULK_LOAD_DATA_GEN_FINISHED_FROM_DATAGEN, buffer.array());
        }   	
        catch (IOException e) {
            e.printStackTrace();
    	}
    }
    
    @Override
    public void receiveCommand(byte command, byte[] data) {
        if (command == VirtuosoSystemAdapterConstants.BULK_LOADING_DATA_FINISHED) {
            LOGGER.info("Getting queries");
            try {            
            	int i=0;
                for (String query: GSBConstants.GSB_QUERIES) {
                    InputStream inputStream = new FileInputStream(GSBConstants.GSB_PATH+"/gsb_queries/" + query);
                    String fileContent = IOUtils.toString(inputStream);
                    fileContent = "#Q-" + (i+1) + "\n" + fileContent; // add a comment line at the beginning of the query, to denote the query number (#Q-1, #Q-2, ...)
                    byte[] bytesArray = null;
                    bytesArray = RabbitMQUtils.writeString(fileContent);
                    sendDataToTaskGenerator(bytesArray);
                    inputStream.close();
                    i++;
                }
                LOGGER.info("Files with queries have been loaded and successfully sent.");
            } catch (IOException ex) {
                System.out.println("Error: " + ex.getMessage());
                ex.printStackTrace();
            }
            generateTasks.release();
        }
        super.receiveCommand(command, data);
    }
}