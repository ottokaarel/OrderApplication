package main;

import org.hsqldb.Server;

import java.io.OutputStream;
import java.io.PrintWriter;

public class HsqlServer {

    public static void main(String[] args) {
        Server server = new Server();
        server.setDatabasePath(0, "mem:db1;sql.syntax_pgs=true");
        server.setDatabaseName(0, "db1");
        server.setLogWriter(new Logger(System.out));
        server.start();
    }

    private static class Logger extends PrintWriter {
        public Logger(OutputStream out) {
            super(out);
        }
    }

}
