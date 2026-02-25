package com.theology.semantic.application.usecases;

import com.theology.semantic.core.domain.TermoDoutrinario;
import java.util.List;

public interface AnalisadorSemanticoGateway {
    List<TermoDoutrinario> analisarConceitos(String textoBruto);
}
