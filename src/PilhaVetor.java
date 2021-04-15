/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author Rodrigo Luís Zimmermann
 */
public class PilhaVetor<T> implements Pilha<T> {

    private int tamanho = 0;
    private T[] info;
    private final int limite;

    //Construtores
    public PilhaVetor(int limite) {
        this.limite = limite;
        info = (T[]) new Object[this.limite];
    }

    public int getTamanho() {
        return tamanho;
    }

    public T[] getInfo() {
        return info;
    }

    //Métodos
    @Override
    public void push(T valor) throws Exception {
        if (tamanho == info.length) {
            expande();
        }
        info[tamanho] = valor;
        tamanho++;
    }

    private void expande() {
        T[] novoVetor = (T[]) new Object[info.length + 1];
        for (int i = 0; i < info.length; i++) {
            novoVetor[i] = info[i];
        }
        info = novoVetor;
    }

    @Override
    public T pop() {
        if (this.estaVazia()) {
            T retorno = (T)"";
            return retorno;
        } else {
            T retorno = info[tamanho - 1];
            info[tamanho - 1] = null;
            tamanho--;
            return retorno;
        }
    }

    @Override
    public T peek() {
        T retorno = null;
        if (this.estaVazia()) {
            retorno = (T) "Pilha Vazia";
        } else {
            retorno = info[tamanho - 1];
        }
        return retorno;
    }

    @Override
    public boolean estaVazia() {
        return tamanho == 0;
    }

    @Override
    public void libera() {
        if (this.estaVazia()) {
            throw new PilhaException("Não há nada na pilha para liberar");
        } else {
        	info[limite] = null;
            tamanho = 0;
        }
    }
    
 
    public String toString() {
    	String retorno = "";
        if (this.estaVazia()) {
            throw new PilhaException("Não há nada na pilha para imprimir");
        } else {
        	for (int i = info.length; i > 0; i--) {
                 retorno += info[i] + ", ";
            }
        }
		return retorno;
    }
    
  
    public void concatenar(PilhaVetor<T> p) {
    	int tamanho = this.tamanho + p.getTamanho();
    	T[] vetor = (T[]) new Object[p.getTamanho()];
    	vetor = p.getInfo();
    	for (int i = this.tamanho + 1 ; i < tamanho; i++) {
    		info[i] = vetor[i];
       }
    }

}
