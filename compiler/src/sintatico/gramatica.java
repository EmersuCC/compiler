package sintatico;
import java.util.*;

import lexico.Token;

public class gramatica {
	boolean valido;
	
	public gramatica() {
		valido = false;
	}
	
	public boolean programa(ArrayList<Token> codigo){
		if(codigo.get(0).getContent().equals(":")) {
			codigo.remove(0);
		} else {
			throw new RuntimeException("Deu erro no :, arruma");
		}
		
		if(codigo.get(0).getContent().equals("DECLARACOES")) {
			codigo.remove(0);
		} else {
			throw new RuntimeException("Deu erro no declaracoes, arruma");
		}
		
		codigo = listaDeclaracoes(codigo);
		
		if(codigo.get(0).getContent().equals(":")) {
			codigo.remove(0);
		} else {
			throw new RuntimeException("Deu erro no :, arruma");
		}
		
		if(codigo.get(0).getContent().equals("ALGORITMO")) {
			codigo.remove(0);
		} else {
			throw new RuntimeException("Deu erro no algoritmo, arruma");
		}
		codigo = listaComandos(codigo);
		
		return true;
	}
	
	public ArrayList<Token> listaComandos(ArrayList<Token> codigo) {
		codigo = comando(codigo);
		codigo = listaComandos2(codigo);
		
		return codigo;
	}

	public ArrayList<Token> listaComandos2(ArrayList<Token> codigo) {
		if(codigo.get(0).getContent().equals(";")) {
			codigo.remove(0);
			return codigo;
		}
		else {
			codigo = listaComandos(codigo);
		}
		return codigo;
	}

	public ArrayList<Token> comando(ArrayList<Token> codigo) {
		if(codigo.get(1).getContent().equals("=")) {
			codigo = comandoAtribuicao(codigo);
		} else if(codigo.get(0).getContent().equals("IF")) {
			codigo = comandoCondicao(codigo);
		} else if(codigo.get(0).getContent().equals("PRINT")) {
			codigo = comandoSaida(codigo);
		} else if(codigo.get(0).getContent().equals("WHILE")) {
			codigo = comandoRepeticao(codigo);
		}
		else {
			throw new RuntimeException("Deu erro no comando, arruma");
		}
		return codigo;
	}

	public ArrayList<Token> comandoCondicao(ArrayList<Token> codigo) {
		if(codigo.get(0).getContent().equals("IF")) {
			codigo.remove(0);
			codigo = expressaoRelacional(codigo);
			if(codigo.get(0).getContent().equals("THEN")) {
				codigo.remove(0);
				codigo = comando(codigo);
				codigo = comandoCondicao2(codigo);
			} else {
				throw new RuntimeException("Deu erro no comandoCondicao, arruma");
			}
		} else {
			throw new RuntimeException("Deu erro no comandoCondicao, arruma");
		}
		return codigo;
	}

	private ArrayList<Token> comandoCondicao2(ArrayList<Token> codigo) {
		if(codigo.get(0).getContent().equals(";")) {
			codigo.remove(0);
			return codigo;
		} else if(codigo.get(0).getContent().equals("ELSE")) {
			codigo.remove(0);
			codigo = comando(codigo);
		} else {
			throw new RuntimeException("Deu erro no comandoCondicao2, arruma");
		}
		return codigo;
	}

	private ArrayList<Token> expressaoRelacional(ArrayList<Token> codigo) {
		codigo = termoRelacional(codigo);
		codigo = expressaoRelacional2(codigo);
		return codigo;
	}

	private ArrayList<Token> expressaoRelacional2(ArrayList<Token> codigo) {
		if(codigo.get(0).getContent().equals(";")){
			codigo.remove(0);
			return codigo;
		} else {
			codigo = operadorBooleano(codigo);
			codigo = expressaoRelacional(codigo);
		}
		return codigo;
	}

	private ArrayList<Token> operadorBooleano(ArrayList<Token> codigo) {
		// TODO Auto-generated method stub
		return null;
	}

	private ArrayList<Token> termoRelacional(ArrayList<Token> codigo) {
		// TODO Auto-generated method stub
		return null;
	}

	public ArrayList<Token> comandoSaida(ArrayList<Token> codigo) {
		if(codigo.get(0).getContent().equals("PRINT")) {
			codigo.remove(0);
			/********************************************************************
			 Falta implementar o tipo STRING
			 */
			if(codigo.get(0).getType().toString().equals("STRING") || codigo.get(0).getType().toString().equals("IDENTIFIER")) {
				codigo.remove(0);
			} else {
				throw new RuntimeException("Deu erro no comandoRepeticao, arruma");
			}
		} else {
			throw new RuntimeException("Deu erro no comandoRepeticao, arruma");
		}
		return codigo;
	}

	public ArrayList<Token> comandoRepeticao(ArrayList<Token> codigo) {
		if(codigo.get(0).getContent().equals("WHILE")) {
			codigo.remove(0);
			codigo = expressaoRelacional(codigo);
			codigo = comando(codigo);
		} else {
			throw new RuntimeException("Deu erro no comandoRepeticao, arruma");
		}
		return codigo;
	}

	public ArrayList<Token> comandoAtribuicao(ArrayList<Token> codigo) {
		if(codigo.get(0).getType().toString().equals("IDENTIFIER")) {
			codigo.remove(0);
			if(codigo.get(0).getContent().equals("=")) {
				codigo.remove(0);
				codigo = expressaoAritmetica(codigo);
			} else {
				throw new RuntimeException("Deu erro no comandoAtribuicao, arruma");
			}
		} else {
			throw new RuntimeException("Deu erro no comandoAtribuicao, arruma");
		}
		return codigo;
	}

	private ArrayList<Token> expressaoAritmetica(ArrayList<Token> codigo) {
		// TODO Auto-generated method stub
		return null;
	}

	public ArrayList<Token> listaDeclaracoes(ArrayList<Token> codigo){
		codigo = declaracao(codigo);
		codigo = listaDeclaracoes2(codigo);
		return codigo;
	}

	public ArrayList<Token> listaDeclaracoes2(ArrayList<Token> codigo) {
		if(codigo.get(0).getContent().equals(";")) {
			codigo.remove(0);
			return codigo;
		}
		else {
			codigo = listaDeclaracoes(codigo);
		}
		return codigo;
	}

	public ArrayList<Token> declaracao(ArrayList<Token> codigo) {
		// TODO Auto-generated method stub
		return null;
	}
}
