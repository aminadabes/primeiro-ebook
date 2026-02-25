import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';

export const authGuard: CanActivateFn = (route, state) => {
  const router = inject(Router);
  const token = localStorage.getItem('semantic_token');

  // Proteção simples por presença de token (Em infra real decodificamos a expiração)
  if (token) {
    return true;
  }

  // Redireciona para login se tentar acessar rotas protegidas (ex: /reader)
  router.navigate(['/login']);
  return false;
};
