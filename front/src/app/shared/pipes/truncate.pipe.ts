import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'truncate'
})
export class TruncatePipe implements PipeTransform {
  // Pipe utile pour tronquer un texte, valeur par défaut à 1000 caractères
  transform(value: string, limit = 1000): string {
    if (value.length <= limit) {
      return value;
    }
    return value.substring(0, limit) + '...';
  }

}
