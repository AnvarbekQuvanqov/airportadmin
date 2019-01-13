/**
 * Created by Jack on 17.12.2018.
 */

import controllers.NavController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import utils.FxmlViews;
import utils.widgets.Utils;
import utils.widgets.Wtransfer;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Scanner;

import static com.sun.xml.internal.ws.api.message.Packet.State.ClientRequest;

/**
 * Created by Jack on 14.12.2018.
 */
public class App extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception
    {

        String cwd = new File("").getAbsolutePath();
        Scanner cine = new Scanner(new File(cwd + "/src/main/resources/multilanguage/Language.txt"));
        String temp = "";
        temp = cine.next();
        System.out.println(temp);
        Locale locale = new Utils().getLocale(temp);
        Wtransfer wtransfer = null;
        if(locale != null){
            wtransfer = new Wtransfer(FxmlViews.Login.loginView,"Admin",locale);
        }
        else
        {
            System.err.println("faylda hato bor qaysi tilni oqishni bilmayabdi locale null qaytyabdi");
            wtransfer =  new Wtransfer(FxmlViews.Login.loginView,"Admin",locale);
        }
     //   wtransfer = new Wtransfer(FxmlViews.Login.loginView,"Admin",locale);
        wtransfer.setStageStyle(StageStyle.DECORATED);
        wtransfer.setResizeble(false);
        wtransfer.setMaximized(false);
        wtransfer.show();
      //  primaryStage.setScene(new Scene(root));
      //  primaryStage.initStyle(StageStyle.DECORATED);
    //   primaryStage.setMaximized(false);
     //   primaryStage.setResizable(false);
      //  primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}


