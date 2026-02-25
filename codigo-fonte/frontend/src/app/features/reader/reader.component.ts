import { Component, signal } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-reader',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="reader-container">
      <header>
        <h2>Status da Análise Teológica: <span class="status-badge">{{ status() }}</span></h2>
        <button (click)="iniciarLeituraIA()">Simular Análise IA</button>
      </header>
      
      <div class="split-pane">
        <section class="pdf-pane">
          <h3>Documento Original</h3>
          <p class="placeholder-text">A extração do PDF aparecerá aqui. (Integração PDFBox futura)</p>
        </section>
        
        <section class="ai-pane">
          <h3>Conceitos Extraídos (Spring AI)</h3>
          <ul>
            <li *ngFor="let termo of termosExtraidos()">
              <strong>{{ termo.categoria }}</strong>: {{ termo.palavra }} (Relevância: {{ termo.peso | percent }})
            </li>
          </ul>
        </section>
      </div>
    </div>
  `,
  styles: [`
    .reader-container { padding: 20px; font-family: 'Georgia', serif; }
    .split-pane { display: flex; gap: 20px; margin-top: 20px; }
    .pdf-pane, .ai-pane { flex: 1; padding: 20px; border: 1px solid #ccc; border-radius: 8px; background-color: #fcfbf9; }
    .status-badge { font-weight: bold; color: #d32f2f; }
    button { padding: 10px 20px; font-size: 16px; cursor: pointer; background-color: #1976d2; color: white; border: none; border-radius: 4px; }
    button:hover { background-color: #115293; }
  `]
})
export class ReaderComponent {
  // Substituindo BehaviorSubject do RxJS por um Signal nativo do Angular 18
  status = signal<'Aguardando' | 'ExtraindoTexto' | 'LendoContextoIA' | 'Concluido'>('Aguardando');
  
  // Lista de termos identificados pela IA (mock)
  termosExtraidos = signal<{ palavra: string; categoria: string; peso: number; }[]>([]);

  iniciarLeituraIA() {
    this.status.set('ExtraindoTexto');
    
    // Simulando a demora da rede / processamento
    setTimeout(() => {
      this.status.set('LendoContextoIA');
      
      setTimeout(() => {
        this.status.set('Concluido');
        this.termosExtraidos.set([
          { palavra: 'Redenção', categoria: 'Soteriologia', peso: 0.95 },
          { palavra: 'Espírito', categoria: 'Pneumatologia', peso: 0.88 },
          { palavra: 'Fim dos Tempos', categoria: 'Escatologia', peso: 0.99 }
        ]);
      }, 1500);
      
    }, 1000);
  }
}
