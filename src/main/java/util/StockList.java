package util;

/**
 * Created by FireAwayH on 14/07/2016.
 */
public class StockList {
    private static String[] allStocks = {"3In", "Aa", "Aal", "Abf", "Aca", "Adm", "Adn", "Agk", "Agr", "Aht", "Ald", "Alm", "Amfw", "Anto", "Ao", "Arm", "Ascl", "Ashm", "Asl", "Atk", "Atst", "Auto", "Av", "Avv", "Azn", "Ba", "Bab", "Bag", "Barc", "Bats", "Bba", "Bbox", "Bby", "Bdev", "Bez", "Bgeo", "Bhmg", "Bkg", "Blnd", "Blt", "Bme", "Bnkr", "Bnzl", "Bok", "Boy", "Bp", "Brby", "Brsn", "Brw", "BtA", "Btem", "Btg", "Bvic", "Bvs", "Bwng", "Bwy", "Byg", "Capc", "Card", "Cbg", "Ccc", "Cch", "Ccl", "Cey", "Cine", "Cir", "Ckn", "Cldn", "Cli", "Clln", "Cmcx", "Cna", "Cne", "Cob", "Cpg", "Cpi", "Crda", "Crh", "Crst", "Csp", "Cty", "Cwd", "Cwk", "Cybg", "Dc", "Dcc", "Dcg", "Deb", "Dfs", "Dge", "Djan", "Dlg", "Dln", "Dnlm", "Dom", "Dph", "Dplm", "Drty", "Drx", "Dty", "Ecm", "Edin", "Elm", "Elta", "Emg", "Erm", "Esnt", "Esur", "Eto", "Evr", "Expn", "Ezj", "Fcpt", "Fcss", "Fdsa", "Fev", "Fgp", "Fgt", "Frcl", "Fres", "Gcp", "Gfrd", "Gfs", "Gftu", "Gkn", "Glen", "Gnc", "Gnk", "Gns", "Gog", "Gpor", "Grg", "Gri", "Gsk", "Gss", "Has", "Hfd", "Hgg", "Hicl", "Hik", "Hils", "Hl", "Hlma", "Hmso", "Hoc", "Home", "Hsba", "Hstg", "Hstn", "Hsv", "Hsx", "Hvpe", "Hwdn", "Iag", "Iap", "Ibst", "Icp", "Igg", "Ihg", "Iii", "Imb", "Imi", "Inch", "Indv", "Inf", "Inpp", "Intu", "Invp", "Ipf", "Ipo", "Isat", "Itrk", "Itv", "Jam", "Jd", "Jdw", "Je", "Jlg", "Jlif", "Jlt", "Jmat", "Jmg", "Jrp", "Jup", "Kaz", "Kgf", "Kie", "Klr", "Kwe", "Lad", "Land", "Lgen", "Lloy", "Lmp", "Lrd", "Lre", "Lse", "Mab", "Mars", "Mcro", "Mcs", "Mdc", "Merl", "Mgam", "Mggt", "Mks", "Mlc", "Mndi", "Mnks", "Mony", "Mrc", "Mrw", "Mslh", "Mto", "Mtro", "Myi", "Nbls", "Ncc", "Nex", "Ng", "Nmc", "Nxt", "Ocdo", "Oml", "Osb", "P2P", "Pag", "Page", "Pay", "Pays", "Pct", "Pdg", "Pets", "Pfc", "Pfg", "Phnx", "Pli", "Plp", "Pnl", "Pnn", "Poly", "Ppb", "Pru", "Psn", "Pson", "Ptec", "Pzc", "Qq", "Rat", "Rb", "Rbs", "Rcp", "Rdi", "Rdsa", "Rdsb", "Rdw", "Rel", "Rgu", "Rio", "Rmg", "Rmv", "Rnk", "Ror", "Rpc", "Rr", "Rrs", "Rsa", "Rse", "Rsw", "Rtn", "Rto", "Sab", "Safe", "Saga", "Sbry", "Scin", "Sct", "Sdr", "Sgc", "Sge", "Sgp", "Sgro", "Shaw", "Shb", "Shi", "Shp", "Skg", "Sky", "Sl", "Smds", "Smin", "Smp", "Smt", "Smwh", "Sn", "Snr", "Soph", "Spd", "Spi", "Spx", "Srp", "Sse", "Sspg", "Stan", "Stj", "Svi", "Svs", "Svt", "Sxs", "Synt", "Talk", "Tate", "Tcg", "Ted", "Tem", "Tep", "Tlpr", "Tlw", "Tmpl", "Tpk", "Trig", "Try", "Tsco", "Tui", "Tw", "Ubm", "Udg", "Ukcm", "Ule", "Ulvr", "Utg", "Uu", "Vct", "Vec", "Ved", "Vm", "Vod", "Vsvs", "Weir", "Wg", "Wizz", "Wkp", "Wmh", "Wos", "Wpct", "Wpg", "Wpp", "Wtan", "Wtb", "Wwh", "Zpla"};
//    public static String[] stocks = {"3In", "Aa", "Aal", "Abf", "Aca", "Adm", "Adn", "Agk", "Agr", "Aht", "Ald", "Alm", "Amfw", "Anto", "Ao", "Arm", "Ashm", "Asl", "Atk", "Atst", "Auto", "Av", "Avv", "Azn", "Ba", "Bab", "Bag", "Barc", "Bats", "Bba", "Bbox", "Bby", "Bdev", "Bgeo", "Bhmg", "Bkg", "Blnd", "Blt", "Bme", "Bnkr", "Bnzl", "Bok", "Boy", "Bp", "Brby", "Brsn", "Brw", "BtA", "Btem", "Btg", "Bvic", "Bvs", "Bwng", "Bwy", "Byg", "Capc", "Cbg", "Ccc", "Cch", "Ccl", "Cey", "Cine", "Cir", "Ckn", "Cldn", "Cli", "Clln", "Cna", "Cne", "Cob", "Cpg", "Cpi", "Crda", "Crh", "Crst", "Cty", "Cwd", "Cwk", "Dc", "Dcc", "Dcg", "Deb", "Dfs", "Dge", "Djan", "Dlg", "Dln", "Dnlm", "Dom", "Dph", "Dplm", "Drty", "Drx", "Dty", "Ecm", "Edin", "Elm", "Elta", "Emg", "Erm", "Esnt", "Esur", "Eto", "Evr", "Expn", "Ezj", "Fcpt", "Fcss", "Fdsa", "Fev", "Fgp", "Fgt", "Frcl", "Fres", "Gcp", "Gfrd", "Gfs", "Gftu", "Gkn", "Glen", "Gnk", "Gns", "Gog", "Gpor", "Grg", "Gri", "Gsk", "Gss", "Has", "Hfd", "Hgg", "Hicl", "Hik", "Hils", "Hl", "Hlma", "Hmso", "Hoc", "Home", "Hsba", "Hstg", "Hstn", "Hsv", "Hsx", "Hvpe", "Hwdn", "Iag", "Iap", "Ibst", "Icp", "Igg", "Ihg", "Iii", "Imb", "Imi", "Inch", "Indv", "Inf", "Inpp", "Intu", "Invp", "Ipf", "Ipo", "Isat", "Itrk", "Itv", "Jam", "Jd", "Jdw", "Je", "Jlg", "Jlif", "Jlt", "Jmat", "Jmg", "Jrp", "Jup", "Kaz", "Kgf", "Kie", "Klr", "Lad", "Land", "Lgen", "Lloy", "Lmp", "Lrd", "Lre", "Lse", "Mab", "Mars", "Mcro", "Mcs", "Mdc", "Merl", "Mgam", "Mggt", "Mks", "Mlc", "Mndi", "Mnks", "Mony", "Mrc", "Mrw", "Mslh", "Mto", "Myi", "Nbls", "Ncc", "Nex", "Ng", "Nmc", "Nxt", "Ocdo", "Oml", "Osb", "P2P", "Pag", "Page", "Pay", "Pays", "Pct", "Pdg", "Pets", "Pfc", "Pfg", "Phnx", "Pli", "Plp", "Pnl", "Pnn", "Poly", "Pru", "Psn", "Pson", "Pzc", "Qq", "Rat", "Rb", "Rbs", "Rcp", "Rdi", "Rdsa", "Rdsb", "Rdw", "Rel", "Rgu", "Rio", "Rmg", "Rmv", "Rnk", "Ror", "Rpc", "Rr", "Rrs", "Rsa", "Rse", "Rsw", "Rtn", "Rto", "Sab", "Safe", "Saga", "Sbry", "Scin", "Sct", "Sdr", "Sgc", "Sge", "Sgp", "Sgro", "Shaw", "Shb", "Shi", "Shp", "Sky", "Sl", "Smds", "Smin", "Smp", "Smt", "Smwh", "Sn", "Snr", "Soph", "Spd", "Spi", "Spx", "Srp", "Sse", "Sspg", "Stan", "Stj", "Svi", "Svs", "Svt", "Sxs", "Synt", "Talk", "Tate", "Tcg", "Ted", "Tem", "Tep", "Tlpr", "Tlw", "Tmpl", "Tpk", "Trig", "Try", "Tsco", "Tui", "Tw", "Ubm", "Udg", "Ukcm", "Ule", "Ulvr", "Utg", "Uu", "Vct", "Vec", "Ved", "Vm", "Vod", "Vsvs", "Weir", "Wg", "Wizz", "Wkp", "Wmh", "Wos", "Wpct", "Wpg", "Wpp", "Wtan", "Wtb", "Wwh", "Zpla"};
    public static String[] stocks = {"3In", "Aa", "Aal", "Abf", "Aca", "Adm", "Adn", "Agk", "Agr", "Aht", "Ald", "Alm", "Amfw", "Anto", "Ao", "Arm", "Ashm", "Asl", "Atk", "Atst", "Auto", "Av", "Avv", "Azn", "Ba", "Bab", "Bag", "Barc", "Bats", "Bba", "Bbox", "Bby", "Bdev", "Bgeo", "Bhmg", "Bkg", "Blnd", "Blt", "Bme", "Bnkr", "Bnzl", "Bok", "Boy", "Bp", "Brby", "Brsn", "Brw", "BtA", "Btem", "Btg", "Bvic", "Bvs", "Bwng", "Bwy", "Byg", "Capc", "Cbg", "Ccc", "Cch", "Ccl", "Cey", "Cine", "Cir", "Ckn", "Cldn", "Cli", "Clln", "Cna", "Cne", "Cob", "Cpg", "Cpi", "Crda", "Crh", "Crst", "Cty", "Cwd", "Cwk", "Dc", "Dcc", "Dcg", "Deb", "Dfs", "Dge", "Djan", "Dlg", "Dln", "Dnlm", "Dom", "Dph", "Dplm", "Drty", "Drx", "Dty", "Ecm", "Edin", "Elm", "Elta", "Emg", "Erm", "Esnt", "Esur", "Eto", "Evr", "Expn", "Ezj", "Fcpt", "Fcss", "Fdsa", "Fev", "Fgp", "Fgt", "Frcl", "Fres", "Gcp", "Gfrd", "Gfs", "Gftu", "Gkn", "Glen", "Gnk", "Gns", "Gog", "Gpor", "Grg", "Gri", "Gsk", "Gss", "Has", "Hfd", "Hgg", "Hicl", "Hik", "Hils", "Hl", "Hlma", "Hmso", "Hoc", "Home", "Hsba", "Hstg", "Hstn", "Hsv", "Hsx", "Hvpe", "Hwdn", "Iag", "Iap", "Ibst", "Icp", "Igg", "Ihg", "Iii", "Imb", "Imi", "Inch", "Indv", "Inf", "Inpp", "Intu", "Invp", "Ipf", "Ipo", "Isat", "Itrk", "Itv", "Jam", "Jd", "Jdw", "Je", "Jlg", "Jlif", "Jlt", "Jmat", "Jmg", "Jrp", "Jup", "Kaz", "Kgf", "Kie", "Klr", "Lad", "Land", "Lgen", "Lloy", "Lmp", "Lrd", "Lre", "Lse", "Mab", "Mars", "Mcro", "Mcs", "Mdc", "Merl", "Mgam", "Mggt", "Mks", "Mlc", "Mndi", "Mnks", "Mony", "Mrc", "Mrw", "Mslh", "Mto", "Myi", "Nbls", "Ncc", "Nex", "Ng", "Nmc", "Nxt", "Ocdo", "Oml", "Osb", "P2P", "Pag", "Page", "Pay", "Pays", "Pct", "Pdg", "Pets", "Pfc", "Pfg", "Phnx", "Pli", "Plp", "Pnl", "Pnn", "Poly", "Pru", "Psn", "Pson", "Pzc", "Qq", "Rat", "Rb", "Rbs", "Rcp", "Rdi", "Rdsa", "Rdsb", "Rdw", "Rel", "Rgu", "Rio", "Rmg", "Rmv", "Rnk", "Ror", "Rpc", "Rr", "Rrs", "Rsa", "Rse", "Rsw", "Rtn", "Rto", "Sab", "Safe", "Saga", "Sbry", "Scin", "Sct", "Sdr", "Sgc", "Sge", "Sgp", "Sgro", "Shaw", "Shb", "Shi", "Shp", "Sky", "Sl", "Smds", "Smin", "Smp", "Smt", "Smwh", "Sn", "Snr", "Soph", "Spd", "Spi", "Spx", "Srp", "Sse", "Sspg", "Stan", "Stj", "Svi", "Svs", "Svt", "Sxs", "Synt", "Talk", "Tate", "Tcg", "Ted", "Tem", "Tep", "Tlpr", "Tlw", "Tmpl", "Tpk", "Trig", "Try", "Tsco", "Tui", "Tw", "Ubm", "Udg", "Ukcm"};

    public static String[] someStocks = {"3In", "Aa", "Aal", "Abf", "Aca", "Adm", "Adn", "Agk", "Agr", "Aht", "Ald"};

}
