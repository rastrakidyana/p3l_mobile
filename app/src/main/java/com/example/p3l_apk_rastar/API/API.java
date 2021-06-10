package com.example.p3l_apk_rastar.API;

public class API {
    public static final String ROOT_URL   = "https:///atmakoreanbbq.online/public/";
    public static final String ROOT_API   = ROOT_URL+"api/";
//    public static final String URL_IMAGE  = ROOT_URL+"images/catalogs/";

    //Pesanan
    public static final String URL_INDEX_PESANAN = ROOT_API+"pesanan_mobile/";  //GET
//    public static final String URL_SHOW_PESANAN = ROOT_API+"pesanan_mobile/";  //GET
    public static final String URL_STORE_PESANAN    = ROOT_API+"pesanan_mobile";  //POST
    public static final String URL_SUBTOTAL_PESANAN    = ROOT_API+"pesanan_subtotal_mobile/";  //GET
//    public static final String URL_UPDATE_PESANAN = ROOT_API+"pesanan/"; //PUT

    //Menu
    public static final String URL_INDEX_MENU = ROOT_API+"menu_mobile";  //GET
    public static final String URL_SHOW_MENU = ROOT_API+"menu_mobile/";  //GET

    //Transaksi
    public static final String URL_SHOW_TRANSAKSI = ROOT_API+"transaksi_mobile/";  //GET

    //Reservasi
    public static final String URL_SCAN_RESERVASI = ROOT_API+"reservasi_scan/";  //GET

}
