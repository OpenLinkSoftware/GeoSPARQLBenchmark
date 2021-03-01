package org.hobbit.geosparql.systems;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

import org.aksw.jena_sparql_api.core.QueryExecutionFactory;
import org.aksw.jena_sparql_api.core.UpdateExecutionFactory;
import org.aksw.jena_sparql_api.core.UpdateExecutionFactoryHttp;
import org.aksw.jena_sparql_api.core.utils.UpdateRequestUtils;
import org.aksw.jena_sparql_api.http.QueryExecutionFactoryHttp;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.update.UpdateRequest;
import org.hobbit.core.components.AbstractSystemAdapter;
import org.hobbit.geosparql.util.VirtuosoSystemAdapterConstants;
import org.hobbit.core.rabbit.RabbitMQUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.jena.atlas.web.auth.HttpAuthenticator;
import org.apache.jena.atlas.web.auth.SimpleAuthenticator;

public class VirtuosoSysAda extends AbstractSystemAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(VirtuosoSysAda.class);
    private String virtuosoContName = "localhost";
    private QueryExecutionFactory queryExecFactory;
    private UpdateExecutionFactory updateExecFactory;

    private boolean dataLoadingFinished = false;
    //SortedSet<String> graphUris = new TreeSet<String>(); 

    private AtomicInteger totalReceived = new AtomicInteger(0);
    private AtomicInteger totalSent = new AtomicInteger(0);
    private Semaphore allDataReceivedMutex = new Semaphore(0);

    private int loadingNumber = 0;
    private String datasetFolderName;

    public VirtuosoSysAda(int numberOfMessagesInParallel) {
        super(numberOfMessagesInParallel);
    }

    public VirtuosoSysAda() {

    }

    @Override
    public void receiveGeneratedData(byte[] arg0) {
        if (dataLoadingFinished == false) {
            ByteBuffer dataBuffer = ByteBuffer.wrap(arg0);    	
            String fileName = RabbitMQUtils.readString(dataBuffer);

            LOGGER.info("Receiving file: " + fileName);

            //graphUris.add(fileName);

            byte [] content = new byte[dataBuffer.remaining()];
            dataBuffer.get(content, 0, dataBuffer.remaining());

            if (content.length != 0) {
                FileOutputStream fos;
                try {
                    if (fileName.contains("/"))
                        fileName = fileName.replaceAll("[^/]*[/]", "");
                    fos = new FileOutputStream(datasetFolderName + File.separator + fileName);
                    fos.write(content);
                    fos.close();
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            if(totalReceived.incrementAndGet() == totalSent.get()) {
                allDataReceivedMutex.release();
            }
        }
        else {			
            ByteBuffer buffer = ByteBuffer.wrap(arg0);
            String insertQuery = RabbitMQUtils.readString(buffer);

            UpdateRequest updateRequest = UpdateRequestUtils.parse(insertQuery);
            try {
                updateExecFactory.createUpdateProcessor(updateRequest).execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void receiveGeneratedTask(String taskId, byte[] data) {
        ByteBuffer buffer = ByteBuffer.wrap(data);
        String queryString = RabbitMQUtils.readString(buffer);
        long timestamp1 = System.currentTimeMillis();
        //LOGGER.info(taskId);
        if (queryString.contains("INSERT DATA")) {
            //TODO: Virtuoso hack
            queryString = queryString.replaceFirst("INSERT DATA", "INSERT");
            queryString += "WHERE { }\n";

            HttpAuthenticator auth = new SimpleAuthenticator("dba", "dba".toCharArray());
            updateExecFactory = new UpdateExecutionFactoryHttp("http://" + virtuosoContName + ":8890/sparql-auth", auth);
            UpdateRequest updateRequest = UpdateRequestUtils.parse(queryString);
            try {
                updateExecFactory.createUpdateProcessor(updateRequest).execute();
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                this.sendResultToEvalStorage(taskId, RabbitMQUtils.writeString(""));
            } catch (IOException e) {
                LOGGER.error("Got an exception while sending results.", e);
            }
        }
        else {
            if (queryString.contains("INFERENCE")) {
                // Activate inference in Virtuoso
                queryString = "DEFINE input:inference <myset>\n" + queryString;
                LOGGER.info("Added an INFERENCE line to a query. This is the new query text:\n" + queryString);
            }
            QueryExecution qe = queryExecFactory.createQueryExecution(queryString);
            ResultSet results = null;
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            try {
                results = qe.execSelect();
                ResultSetFormatter.outputAsJSON(outputStream, results);
            } catch (Exception e) {
                LOGGER.info("Problem while executing task " + taskId + ": " + queryString);
                //TODO: fix this hacking
                try {
                    outputStream.write("{\"head\":{\"vars\":[\"xxx\"]},\"results\":{\"bindings\":[{\"xxx\":{\"type\":\"literal\",\"value\":\"XXX\"}}]}}".getBytes());
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                e.printStackTrace();
            } finally {
                qe.close();
            }

            try {
                this.sendResultToEvalStorage(taskId, outputStream.toByteArray());
//              LOGGER.info(new String(outputStream.toByteArray()));
//              LOGGER.info("--------------------");
            } catch (IOException e) {
                LOGGER.error("Got an exception while sending results.", e);
            }
        }
        long timestamp2 = System.currentTimeMillis();
        //LOGGER.info("Task " + taskId + ": " + (timestamp2-timestamp1));
    }

    @Override
    public void init() throws Exception {
        LOGGER.info("Initialization begins.");
        super.init();
        internalInit();
        LOGGER.info("Initialization is over.");
    }

    private void internalInit() {
        datasetFolderName = "/myvol/datasets";
        File theDir = new File(datasetFolderName);
        theDir.mkdir();

        queryExecFactory = new QueryExecutionFactoryHttp("http://" + virtuosoContName + ":8890/sparql");

        // create update factory
        HttpAuthenticator auth = new SimpleAuthenticator("dba", "dba".toCharArray());
        updateExecFactory = new UpdateExecutionFactoryHttp("http://" + virtuosoContName + ":8890/sparql-auth", auth);
    }

    @Override
    public void receiveCommand(byte command, byte[] data) {

        if (VirtuosoSystemAdapterConstants.BULK_LOAD_DATA_GEN_FINISHED == command) {

            ByteBuffer buffer = ByteBuffer.wrap(data);
            int numberOfMessages = buffer.getInt();
            boolean lastBulkLoad = buffer.get() != 0;

            LOGGER.info("Bulk loading phase (" + loadingNumber + ") begins");

            // if all data have been received before BULK_LOAD_DATA_GEN_FINISHED command received
            // release before acquire, so it can immediately proceed to bulk loading
            if(totalReceived.get() == totalSent.addAndGet(numberOfMessages)) {
                allDataReceivedMutex.release();
            }

            LOGGER.info("Wait for receiving all data for bulk load " + loadingNumber + ".");
            try {
                allDataReceivedMutex.acquire();
            } catch (InterruptedException e) {
                LOGGER.error("Exception while waiting for all data for bulk load " + loadingNumber + " to be received.", e);
            }
            LOGGER.info("All data for bulk load " + loadingNumber + " received. Proceed to the loading...");

//            for (String uri : this.graphUris) {
//                String create = "CREATE GRAPH " + "<" + uri + ">";
//                LOGGER.info(create);
//                UpdateRequest updateRequest = UpdateRequestUtils.parse(create);
//                updateExecFactory.createUpdateProcessor(updateRequest).execute();
//            }
//            this.graphUris.clear();

            loadDataset("http://graph.version." + loadingNumber);

            try {
                sendToCmdQueue(VirtuosoSystemAdapterConstants.BULK_LOADING_DATA_FINISHED);
            } catch (IOException e) {
                e.printStackTrace();
            }

            LOGGER.info("Bulk loading phase (" + loadingNumber + ") is over.");

            loadingNumber++;

            File theDir = new File(datasetFolderName);
            for (File f : theDir.listFiles())
                f.delete();

            if (lastBulkLoad) {
                dataLoadingFinished = true;
                LOGGER.info("All bulk loading phases are over.");
            }
        }
        super.receiveCommand(command, data);
    }

    private void loadDataset(String graphURI) {
        String scriptFilePath = System.getProperty("user.dir") + File.separator + "load.sh";
        String[] command = {"/bin/bash", scriptFilePath, virtuosoContName, datasetFolderName, "8", graphURI};
        Process p;
        try {
            p = new ProcessBuilder(command).redirectErrorStream(true).start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            p.waitFor();
            String line = null;
            while ( (line = reader.readLine()) != null) {
                LOGGER.info(line);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void close() throws IOException {
        try {
            queryExecFactory.close();
            updateExecFactory.close();
        } catch (Exception e) {
        }
//        try {
//            TimeUnit.SECONDS.sleep(10);
//        } catch (InterruptedException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
        super.close();
        LOGGER.info("Virtuoso has stopped.");
    }
}
