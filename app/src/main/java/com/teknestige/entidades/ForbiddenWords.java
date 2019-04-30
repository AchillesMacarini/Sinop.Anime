package com.teknestige.entidades;

public class ForbiddenWords {

    String[] palavras;

    public ForbiddenWords(){}

    public ForbiddenWords(String []palavras){
        this.palavras=palavras;
        setPalavras();
    }
//polenta
    public void setPalavras(){
        this.palavras= new String []
                {"quero saber",
                "que",
                "do",
                "um",
                "de",
                "onde",
                "o",
                "os",
                "a",
                "as",
                "uns",
                "uma",
                "umas",
                "cujo",
                "cuja",
                "cujos",
                "cujas",
                "qual",
                "quais",
                "eles",
                "ele",
                "ela",
                "elas",
                "eu",
                "tu",
                "você",
                "nós",
                "vós",
                "vocês",
                "isso",
                "já",
                "aquilo",
                "isto",
                "esta",
                "este",
                "essa",
                "esse",
                "estes",
                "esses",
                "essas",
                "estas",
                "aquele",
                "aqueles",
                "aquela",
                "aquelas",
                "havia",
                "há",
                "houve",
                "haviam",
                "haviamos",
                "haverão",
                "haverá",
                "e",
                "com",
                "antes",
                "em",
                "tinha",
                "tem",
                "têm",
                "entre",
                "como",
                "no",
                "nos",
                "na",
                "nas",
                "me",
                "se",
                "te",
                "tchê",
                "anime",
                "muito",
                "muitos",
                "muita"};
    }

    public String[] getPalavras(){
        return palavras;
    }

}
