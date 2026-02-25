# Capítulos 7 e 8: Arquitetura do Frontend e a Experiência

A utilidade de extrair termos doutrinários complexos de PDFs despenca se o usuário final (um Estudante ou Pastor) tiver que interagir com uma tela carregada, lenta e confusa. Nossa aplicação deve focar no essencial: **A Leitura**.

Para entregarmos uma Single Page Application (SPA) que carrega instantaneamente, escolhemos o **Angular 18**.

## 7.1 Arquitetura de Componentes Standalone

A antiga árvore de módulos (`@NgModule`) onde dezenas de componentes ficavam acoplados ficou no passado. Adotamos o padrão **Standalone Components**.

A nossa estrutura no `frontend/src/app` reflete os fluxos do usuário:
- **`features/upload`**: Onde o PDF é solto (Drag and Drop).
- **`features/dashboard`**: A listagem de PDFs já processados.
- **`features/reader`**: A tela principal, onde o PDF aparece de um lado original, e os "Conceitos Teológicos" processados via IA no outro.

Como são Standalone, quando o usuário acessa o `/dashboard`, o Browser só faz o download dos arquivos JavaScript daquela tela. O carregamento é muito mais rápido (*Lazy Loading Nativo*).

## 7.2 O Fim do Zone.js e o Início dos Signals

Como pontuado no Capítulo 1, monitorar mutações no Document Object Model (DOM) de um leitor de texto gigante consumiria muita CPU. 
Com a classe de `Signals` no Angular 18, nós vinculamos variáveis diretamente ao HTML sem que o Angular precise "adivinhar" o que mudou.

```typescript
// Exemplo no nosso ReaderComponent.ts
import { Component, signal } from '@angular/core';

@Component({
  selector: 'app-reader',
  standalone: true,
  template: `
    <div>
      <h2>Status da Leitura IA: {{ status() }}</h2>
      <button (click)="finalizarLeitura()">Marcar Visto</button>
    </div>
  `
})
export class ReaderComponent {
  // Criamos o estado de forma reativa:
  status = signal('Processando...');

  finalizarLeitura() {
    // Quando mudamos o valor, APENAS a tag <h2> acima é repintada no navegador!
    this.status.set('Concluído!');
  }
}
```

## 8.1 UI/UX: Material Design focado na Leitura

Não vamos reinventar componentes visuais. O **Angular Material** nos fornecerá botões, tabelas e cards validados por padrões de acessibilidade globais.

Porém, faremos uma **Tematização Customizada** (Theming). Livros de teologia geralmente usam fontes com Serifa (como *Merriweather* ou *Lora*), com fundos de cor "Papiro" ou Off-White para não cansar a visão do estudante.

Nas páginas seguintes do nosso desenvolvimento iterativo, substituiremos a cara padrão do Angular por uma interface limpa, dividida na funcionalidade de "Dual-Pane" - onde a esquerda contém o PDF bruto em rolagem, e a direita contém a listagem de nuvem de palavras doutrinárias com filtros de relevância calculados pela IA!
