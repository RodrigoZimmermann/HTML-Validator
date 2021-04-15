/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Rodrigo Luís Zimmermann
 */
import java.awt.EventQueue;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Apresentacao {

    private JFrame frame;
    private JTextField txtCaminhoArquivo;
    private static String pvException = "";
    private static String pv = "";
    private static String mensagemTela = "";

    /**
     * Launch the application.
     */
    public static void main(String[] args) throws IOException, Exception {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Apresentacao window = new Apresentacao();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    
    public static void Execute(String diretorio) throws Exception {
    	  PilhaVetor<String> pv = new PilhaVetor<String>(1);
          PilhaVetor<String> pvException = new PilhaVetor<String>(1);
          PilhaVetor<String> pvExceptionRetorno = new PilhaVetor<String>(1);
          PilhaVetor<String> pilhaDeLeitura = new PilhaVetor<String>(1);
          String texto = "";
          String armazena = "";
          String armazenaException = "";
          String tagFinalException = "As seguintes tags não foram fechadas: ";
          String tagFinal = "A ocorrência das tags foram: ";
          int controle = 0;
          texto = AnalisaString(diretorio).replaceAll("\n", "").replaceAll("\r", "");

          for (int i = 0; i < texto.length(); i++) {
              if (texto.charAt(i) == '<') {
                  controle = 1;
                  armazena += texto.charAt(i);
              } else if (texto.charAt(i) == ' ') {
                  armazena += " ";
              } else if (texto.charAt(i) != '>' && controle == 1) {
                  armazena += texto.charAt(i);
              } else if (controle == 1) {
                  controle = 0;
                  armazena += texto.charAt(i);
                  System.out.print(armazena);
                  // aqui chegará a String pronta para ser incluida na pilha, lembrando que ao realizar essa inserção é necessário realizar a comparação e 
                  // contagem das tags
                  if (armazena.replaceAll(" ", "").startsWith("</")) {
                      if (pvException.peek().equalsIgnoreCase(armazena.replaceAll("<", "").replaceAll("/", "").replaceAll(">", "").replaceAll(" ", ""))
                    		  || pvException.peek().equalsIgnoreCase(armazenaException.replaceAll("<", "").replaceAll("/", "").replaceAll(">", "").replaceAll(" ", ""))) {
                          pvException.pop();
                          //necessário  realizar implementação de contador
                      } else  {
                    	  pvExceptionRetorno.push(pvException.pop());
                      }
                  } else {
                      if (!isSingleTag(armazena.replaceAll("<", "").replaceAll(">", "").replaceAll(" ", ""))) {
                          pvException.push(armazena.replaceAll("<", "").replaceAll(">", "").replaceAll(" ", ""));
                      }
                      pv.push(armazena.replaceAll("<", "").replaceAll(">", "").replaceAll(" ", ""));
                  }
                  pilhaDeLeitura.push(armazena);
                  armazenaException = armazena;
                  armazena = "";
              }
          }
          if (pvException.estaVazia()) {
              Apresentacao.mensagemTela = "O arquivo está bem formatado";
              Apresentacao.pv = ValidaPilha(pv, tagFinal);
          } else {
              Apresentacao.mensagemTela = "O arquivo está mal formatado";
              Apresentacao.pvException = ValidaExceptionRetorno(pvExceptionRetorno, tagFinalException);
          }
    }

    public static String ValidaPilha(PilhaVetor pv, String retorno) throws Exception {
        PilhaVetor<String> pvCopia = new PilhaVetor(1);
        String tag = (String) pv.pop();
        int count = 1;
        while (!pv.estaVazia()) {
            if (pv.peek().equals(tag)) {
                count++;
                pv.pop();
            } else {
                if (!pv.peek().equals("")) {
                    pvCopia.push((String) pv.pop());
                }
            }
        }
        if (!(tag == null)) {
            retorno += "\n"+ tag + " " + count;
        }
        if (!pvCopia.estaVazia()) {
            return ValidaPilha(pvCopia, retorno);
        } else {
            return retorno;
        }
    }

    public static String ValidaExceptionRetorno(PilhaVetor pv, String retorno) throws Exception {
        PilhaVetor<String> pvCopia = new PilhaVetor(1);
        String tag = (String) pv.pop();
        int count = 1;
        while (!pv.estaVazia()) {
            if (pv.peek().equals(tag)) {
                count++;
                pv.pop();
            } else {
                if (!pv.peek().equals("")) {
                    pvCopia.push((String) pv.pop());
                }
            }
        }
        if (!(tag == null)) {
        	retorno += "\n"+ tag + " " + count;
        }
        if (!pvCopia.estaVazia()) {
            return ValidaPilha(pvCopia, retorno);
        } else {
            return retorno;
        }
    }

    public static boolean isSingleTag(String valor) {
        switch (valor) {
            case "meta":
                return true;
            case "base":
                return true;
            case "br":
                return true;
            case "col":
                return true;
            case "command":
                return true;
            case "embed":
                return true;
            case "hr":
                return true;
            case "img":
                return true;
            case "input":
                return true;
            case "link":
                return true;
            case "param":
                return true;
            case "source":
                return true;
            case "!DOCTYPE":
                return true;
        }
        return false;
    }

    // método de separação de tags e conversão para String
    public static String AnalisaString(String diretorio) throws Exception {
        String texto = "";
        String aux = "";
        int c = 0;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(diretorio);
        } catch (FileNotFoundException ex) {
            System.out.println("deu pau");
        }
        while ((c = fis.read()) != -1) {
        	aux = String.valueOf((char) c);
            for (int i = 0; i < aux.length(); i++) {
                if (aux.charAt(i) != ' ') {
                    texto += aux;
                } else {
                    texto += aux + ">";
                }
            }
        }
        return texto;
    }

    /**
     * Create the application.
     */
    public Apresentacao() {
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
    	JOptionPane.showMessageDialog(null, "Informe o diretório do arquivo! Exemplo: C:\\\\Users\\\\rodri\\\\Desktop\\\\arquivo.txt");
        frame = new JFrame();
        frame.setBounds(100, 100, 490, 333);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        JLabel lblNewLabel = new JLabel("Arquivo:");
        lblNewLabel.setBounds(10, 15, 46, 14);
        frame.getContentPane().add(lblNewLabel);

        txtCaminhoArquivo = new JTextField();
        txtCaminhoArquivo.setBounds(71, 12, 260, 20);
        frame.getContentPane().add(txtCaminhoArquivo);
        txtCaminhoArquivo.setColumns(10);

        JTextArea txtAreaMensagem = new JTextArea();
        txtAreaMensagem.setEditable(false);
        txtAreaMensagem.setBounds(43, 51, 368, 98);
        frame.getContentPane().add(txtAreaMensagem);

        JScrollPane scroll = new JScrollPane(txtAreaMensagem);
        scroll.setBounds(43, 51, 368, 98);
        frame.getContentPane().add(scroll);

        JTextArea txtAreaTags = new JTextArea();
        txtAreaTags.setEditable(false);
        txtAreaTags.setBounds(43, 173, 370, 98);
        frame.getContentPane().add(txtAreaTags);

        JScrollPane scrollDois = new JScrollPane(txtAreaTags);
        scrollDois.setBounds(43, 173, 370, 98);
        frame.getContentPane().add(scrollDois);

        JButton btnAnalisar = new JButton("Analisar");
        btnAnalisar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
            	try {
					Apresentacao.Execute(txtCaminhoArquivo.getText());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(null, "Erro no caminho do arquivo!");
				}
                txtAreaMensagem.setText(mensagemTela);
                String msg = pv;
                msg += pvException;
                txtAreaTags.setText(msg);

            }
        });
        btnAnalisar.setBounds(363, 11, 89, 23);
        frame.getContentPane().add(btnAnalisar);

    }
}
