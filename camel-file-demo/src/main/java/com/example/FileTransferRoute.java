package com.example;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.main.Main;

public class FileTransferRoute extends RouteBuilder {

    public static void main(String[] args) throws Exception {
        Main main = new Main();
        main.configure().addRoutesBuilder(new FileTransferRoute());
        main.run();
    }

    @Override
    public void configure() {
        from("file:C:/Users/User/Downloads/apache-camel-4.14.1/input?noop=true&idempotent=true")
            .log("[${date:now:yyyy-MM-dd HH:mm:ss}] Procesando archivo: ${file:name}")
            .filter(header("CamelFileName").endsWith(".csv"))
                .convertBodyTo(String.class)
                .transform().simple("${body.toUpperCase()}")
                .multicast()
                    // Copia transformada a output
                    .to("file:C:/Users/User/Downloads/apache-camel-4.14.1/output?fileName=${file:name}")
                    // Copia a archived con timestamp
                    .to("file:C:/Users/User/Downloads/apache-camel-4.14.1/archived?fileName=${file:onlyname.noext}-${date:now:yyyyMMdd-HHmmss}.${file:ext}")
                .end()
            .end();
    }
}
