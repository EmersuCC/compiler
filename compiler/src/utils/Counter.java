package utils;

public class Counter{
    private int nLine;
    private int nColumn;
    //EmptyConstructor
    public Counter(){
        this.nLine = 1;
        this.nColumn = 0;
    }

    
    public void countLineColumn(char currentChar){
        if(currentChar == '\n'){
            this.nLine++;
            this.nColumn = 0;
        }
        else
            this.nColumn++;
    }
    
    public int getnLine() {
        return nLine;
    }
    
    public int getnColumn() {
        return nColumn;
    }

    public void setnLine(int nLine) {
        this.nLine = nLine;
    }


    public void setnColumn(int nColumn) {
        this.nColumn = nColumn;
    }
    
    public void decColumn(){
        this.nColumn--;
    }

    public void decLine(){
        this.nLine--;
    }

    public void incColumn(){
        this.nColumn++;
    }

    public void incLine(){
        this.nLine++;
    }

}
