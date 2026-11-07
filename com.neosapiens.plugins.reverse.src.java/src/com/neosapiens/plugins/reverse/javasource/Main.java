package com.neosapiens.plugins.reverse.javasource;

import com.neosapiens.plugins.reverse.javasource.actions.OutputSemanticAction;
import com.neosapiens.plugins.reverse.javasource.actions.SemanticAction;
import com.neosapiens.plugins.reverse.javasource.parsing.Java1_6Lexer;
import com.neosapiens.plugins.reverse.javasource.parsing.Java1_6Parser;
import org.antlr.runtime.ANTLRFileStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.TokenStream;

import java.io.File;
import java.io.IOException;

public class Main {

    /**
     * @param args
     * @throws IOException 
     * @throws RecognitionException 
     */
    public static void main(String[] args) throws IOException, RecognitionException {

        String workingDir = System.getProperty("user.dir");
        System.out.println("Répertoire de travail courant : " + workingDir);

        //Définit les chemins relatifs basés sur VOTRE structure réelle (src/com/...)

        /**
         helloworldtest.java
         GrammarTestInterfaceDeclaration.java
         EnumerationTest.java
         ReverseJavaSourceCodeWorker.java
         **/

        //Cas A : Si l'IDE/Gradle lance le Main depuis la racine du module
        //(com.neosapiens.plugins.reverse.src.java)
        String cheminRelatifModule = "src/com/neosapiens/plugins/reverse/javasource/actions/SemanticAction.java";

        //Cas B : Si l'IDE/Gradle lance le Main depuis la racine globale du projet
        //(OpenModelSphere)
        String cheminRelatifGlobal = "com.neosapiens.plugins.reverse.src.java/src/com/neosapiens/plugins/reverse/javasource/actions/SemanticAction.java";

        //On cherche le fichier dans le premier cas, puis on fait un fallback sur le deuxième
        File file = new File(workingDir, cheminRelatifModule);

        if (!file.exists()) {
            System.out.println("Fichier non trouvé via le chemin module, essai du chemin global...");
            file = new File(workingDir, cheminRelatifGlobal);
        }

        //Vérification finale
        if (!file.exists()) {
            System.err.println("Fichier introuvable dans les deux emplacements !");
            System.err.println("Dernier chemin testé : " + file.getAbsolutePath());
            return;
        }

        System.out.println("Fichier trouvé : " + file.getAbsolutePath());

        //Création du flux ANTLR
        ANTLRFileStream fileStream = new ANTLRFileStream(file.getAbsolutePath(), "UTF-8");

        Java1_6Lexer lexer = new Java1_6Lexer(fileStream);
        TokenStream tokenStream = new CommonTokenStream(lexer);
        SemanticAction semAct = new OutputSemanticAction();
        Java1_6Parser parser = new Java1_6Parser(tokenStream,semAct);
        parser.compilationUnit();

        System.out.println("done!");
    }

}
