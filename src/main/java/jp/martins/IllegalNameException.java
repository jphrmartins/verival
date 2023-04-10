package jp.martins;

public class IllegalNameException extends RuntimeException{
    public IllegalNameException(){
        super("Nome invaido!");
    }
}
