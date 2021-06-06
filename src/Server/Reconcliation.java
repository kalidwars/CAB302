package Server;

import COMMON.BuyOrder;
import COMMON.SellOrder;
import Server.DataSource;

import javax.xml.crypto.Data;
import java.util.ArrayList;

public class Reconcliation {

    public Reconcliation() {

    }

    public void Reconcile() {
        DataSource DataSource = new DataSource();
        ArrayList<BuyOrder> buyOrders = DataSource.GetBuyOrders();
        ArrayList<SellOrder> sellOrders = DataSource.GetSellOrders();
        for(BuyOrder border : buyOrders) {
            for(SellOrder sorder : sellOrders) {
                if(border.GetName().equals(sorder.GetName()) && border.getIndPrice() == sorder.getIndPrice()) {
                    //Store Infomration for later use
                    String AssetName = border.GetName();
                    String SellUN = sorder.GetUser();
                    String BuyUN = border.GetUser();
                    String SellOU = sorder.GetOUID();
                    String BuyOU = border.GetOUID();
                    int QTY = border.getNumAvailable();
                    double Price = border.getNumAvailable() * sorder.getIndPrice();
                    DataSource.AddTrade(AssetName,SellUN,BuyUN,SellOU,BuyOU,QTY,Price,border.getNumAvailable(),sorder.getNumAvailable());
                    DataSource.CleanUpAssets();
                }
            }
        }
    }
}
