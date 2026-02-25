package com.theology.semantic.application.usecases;

import java.io.InputStream;

public interface ExtratorPdfGateway {
    String extrairTexto(InputStream fileStream);
}
