package main.java.com.sim.network;

import desmoj.core.report.Message;
import desmoj.core.report.MessageReceiver;
import desmoj.core.report.OutputType;
import desmoj.core.report.Reporter;
import desmoj.extensions.xml.report.XMLTraceOutput;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class LogsWritrer implements OutputType {


    private BufferedWriter writer;

    public  LogsWritrer() throws IOException {
        writer = new BufferedWriter(new FileWriter("out_java.csv"));
        writer.write("time, actions \n");
    }

    @Override
    public void receive(Message message) {

        try {
            writer.write(message.getTime() + "," + message.getDescription() + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

        //super.receive(message);

    }

    @Override
    public void receive(Reporter reporter) {

    }

    @Override
    public void open(String s, String s1) {

    }

    @Override
    public void close() {
        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getAppendix() {
        return null;
    }

    //    private  BufferedWriter out = null;
//
//
//    public LogsWritrer() throws IOException {
//        FileWriter fstream = null;
//
//        try {
//            fstream = new FileWriter("out.txt", true);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        out = new BufferedWriter(fstream);
//        out.write("test");
//
//    }
//
//    @Override
//    public void receive(Message message) {
//        try {
//            out.write(message.getTime());
//            out.write(message.getDescription());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public void receive(Reporter reporter) {
//        try {
//            out.write(reporter.getDescription());
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    public void closeLog() throws IOException {
//        out.close();
//    }
}
