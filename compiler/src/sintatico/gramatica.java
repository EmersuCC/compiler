package sintatico;
import java.util.*;
import exceptions.SyntaxException;

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
			throw new SyntaxException("Faltou :");
		}
		
		if(codigo.get(0).getContent().equals("DECLARACOES")) {
			codigo.remove(0);
		} else {
			throw new SyntaxException("Faltaram as declara��es");
		}
		
		codigo = listaDeclaracoes(codigo);
		
		if(codigo.get(0).getContent().equals(":")) {
			codigo.remove(0);
		} else {
			throw new SyntaxException("Faltou :");
		}
		
		if(codigo.get(0).getContent().equals("ALGORITMO")) {
			codigo.remove(0);
		} else {
			throw new SyntaxException("Faltou o algoritmo");
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
			throw new SyntaxException("Nao eh um comando valido");
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
				throw new SyntaxException("Faltou then");
			}
		} else {
			throw new SyntaxException("Faltou if");
		}
		return codigo;
	}

	public ArrayList<Token> comandoCondicao2(ArrayList<Token> codigo) {
		if(codigo.get(0).getContent().equals(";")) {
			codigo.remove(0);
			return codigo;
		} else if(codigo.get(0).getContent().equals("ELSE")) {
			codigo.remove(0);
			codigo = comando(codigo);
		} else {
			throw new SyntaxException("Faltou else");
		}
		return codigo;
	}

	public ArrayList<Token> expressaoRelacional(ArrayList<Token> codigo) {
		codigo = termoRelacional(codigo);
		codigo = expressaoRelacional2(codigo);
		return codigo;
	}

	public ArrayList<Token> expressaoRelacional2(ArrayList<Token> codigo) {
		if(codigo.get(0).getContent().equals(";")){
			codigo.remove(0);
			return codigo;
		} else {
			codigo = operadorBooleano(codigo);
			codigo = expressaoRelacional(codigo);
		}
		return codigo;
	}

	public ArrayList<Token> operadorBooleano(ArrayList<Token> codigo) {
		if(codigo.get(0).getContent().equals("E") || 
		   codigo.get(0).getContent().equals("OU")) {
			codigo.remove(0);
		}
		else {
			throw new SyntaxException("Nao eh um operador booleano");
		}
		return codigo;
	}

	public ArrayList<Token> termoRelacional(ArrayList<Token> codigo) {
		if(codigo.get(0).getContent().equals("(")) {
			codigo.remove(0);
			codigo = expressaoRelacional(codigo);
			if(codigo.get(0).getContent().equals(")")) {
				codigo.remove(0);
			} else {
				throw new SyntaxException("Faltou )");		
			}
		} else {
			codigo = expressaoAritmetica(codigo);
			if(codigo.get(0).getType().toString().equals("RELATIONAL_OPERATOR")) {
				codigo = expressaoAritmetica(codigo);
			} else {
				throw new SyntaxException("Nao e operador relacional");	
			}
		}
		return codigo;
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
				throw new SyntaxException("N�o � poss�vel exibir algo que n�o seja texto ou vari�vel");
			}
		} else {
			throw new SyntaxException("Faltou o print");
		}
		return codigo;
	}

	public ArrayList<Token> comandoRepeticao(ArrayList<Token> codigo) {
		if(codigo.get(0).getContent().equals("WHILE")) {
			codigo.remove(0);
			codigo = expressaoRelacional(codigo);
			codigo = comando(codigo);
		} else {
			throw new SyntaxException("Faltou while");
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
				throw new SyntaxException("Faltou =");
			}
		} else {
			throw new SyntaxException("O lado esquerdo da fun��o assign n�o � uma vari�vel");
		}
		return codigo;
	}

	public ArrayList<Token> expressaoAritmetica(ArrayList<Token> codigo) {
		codigo = termoAritmetico(codigo);
		codigo = expressaoAritmetica2(codigo);
		return codigo;
	}

	public ArrayList<Token> expressaoAritmetica2(ArrayList<Token> codigo) {
		if(codigo.get(0).getContent().equals(";")){ 
			codigo.remove(0);
			return codigo;
		} else {
			codigo = expressaoAritmetica3(codigo);
			codigo = expressaoAritmetica2(codigo);
		}
		return codigo;
	}

	public ArrayList<Token> expressaoAritmetica3(ArrayList<Token> codigo) {
		if(codigo.get(0).getContent().equals("+") || codigo.get(0).getContent().equals("-")){ 
			codigo.remove(0);
			codigo = termoAritmetico(codigo);
		}
		return codigo;
	}

	public ArrayList<Token> termoAritmetico(ArrayList<Token> codigo) {
		codigo = fatorAritmetico(codigo);
		codigo = termoAritmetico2(codigo);
		return codigo;
	}

	public ArrayList<Token> termoAritmetico2(ArrayList<Token> codigo) {
		if(codigo.get(0).getContent().equals(";")){ 
			codigo.remove(0);
			return codigo;
		} else {
			codigo = termoAritmetico3(codigo);
			codigo = termoAritmetico2(codigo);
		}
		return codigo;
	}

	public ArrayList<Token> termoAritmetico3(ArrayList<Token> codigo) {
		if(codigo.get(0).getContent().equals("*") || codigo.get(0).getContent().equals("/")){ 
			codigo.remove(0);
			codigo = termoAritmetico(codigo);
		}
		return codigo;
	}

	public ArrayList<Token> fatorAritmetico(ArrayList<Token> codigo) {
		if(codigo.get(0).getContent().equals("(")) {
			codigo.remove(0);
			codigo = expressaoAritmetica(codigo);
			if(codigo.get(0).getContent().equals(")")) {
				codigo.remove(0);
			}
			else {
				throw new SyntaxException("Faltou )");	
			}
		}
		else if(codigo.get(0).getType().toString().equals("NUMBER_INTEGER") ||
				codigo.get(0).getType().toString().equals("NUMBER_DECIMAL") ||
				codigo.get(0).getType().toString().equals("IDENTIFIER")) {
			codigo.remove(0);
		}
		return codigo;
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
		codigo = tipoVar(codigo);
		if(codigo.get(0).getContent().equals(":")) {
			codigo.remove(0);
			if(codigo.get(0).getType().toString().equals("IDENTIFIER")) {
				codigo.remove(0);
			}
			else {
				throw new SyntaxException("Nome inv�lido");	
			}
		} 
		else {
			throw new SyntaxException("Faltou :");
		}
		return codigo;
	}

	public ArrayList<Token> tipoVar(ArrayList<Token> codigo) {
		if(codigo.get(0).getType().toString().equals("NUMBER_INTEGER") ||
		   codigo.get(0).getType().toString().equals("NUMBER_DECIMAL")) {
			codigo.remove(0);
		}
		else {
			throw new SyntaxException("Tipo inv�lido");
		}
		return codigo;
	}
}
