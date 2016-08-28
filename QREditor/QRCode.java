//QR-Code Maker
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.WindowAdapter; 
import java.awt.event.WindowEvent;
import java.awt.image.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.Hashtable;

import java.awt.event.KeyEvent;

class QRCode extends JFrame implements ActionListener{

	////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////フィールド//////////////////////////////////////////
	//DataBaseからデータ取得
		public static final int NO_DATA = DataBase.NO_DATA;					//定数(データ無)
		public static final int BLACK_DATA = DataBase.BLACK_DATA;			//定数(黒データ)
		public static final int WHITE_DATA = DataBase.WHITE_DATA;			//定数(白データ)
		public static final int K_DATA = DataBase.K_DATA;					//定数(形式、型番予定)
		public static final int DATA_1 = DataBase.DATA_1;					//定数(コードデータ1)
		public static final int DATA_0 = DataBase.DATA_0;					//定数(コードデータ0)
		public static final int DATA_VER = DataBase.DATA_VER;				//定数(型番情報)
		public static final int DATA_K = DataBase.DATA_K;					//定数(形式情報)
		public static final Color TITLE_GRAY = DataBase.TITLE_GRAY;			//定数(タイトル画面背景色)
		public static final Color TITLE_GRAY2 = DataBase.TITLE_GRAY2;		//定数(タイトル画面背景色2)
		public static final String BR = DataBase.BR;						//定数(改行コード)
		public static final String GRAP_SEPARATOR=DataBase.GRAP_SEPARATOR;	//定数(グラフィック部の分離符を格納)
		public static final String BR_IO=DataBase.BR_IO;					//定数(ファイル入出力時の改行コードの置き換え文字)
		int[][][] versionMaxLen=DataBase.getVersionMaxLen();				//QRコード仕様DB
		char[] eisuutaiou=DataBase.eisuutaiou;								//英数字モードの文字対応表DB
		int[] dataA=DataBase.dataA;											//GF(2^8)の指数表現の対応DB
	//ファイルの入出力
		JFileChooser chooser = new JFileChooser();							//ファイルの入出力
	//GUIオブジェクト宣言
		JLabel StatusBar;													//ステータスバーもどき
		JButton b;															//変換ボタン
		JButton inB;														//開くボタン
		JButton editorB;													//エディタを開くボタン
		JButton clearB;
                public static JTextArea inputD;											//入力エリア
		static JTextArea info;												//情報表示エリア
		JScrollPane inputDS;												//入力エリアのスクロールバー
		JScrollPane infoS;													//情報表示エリアのスクロールバー
		static JComboBox verList;											//型番リスト
		static JComboBox maskList;											//マスクリスト
		static JComboBox lvList;											//訂正レベルリスト
		static JComboBox sizeList;
                QRShow[] qrs=new QRShow[simbolNumMax];								//QRコード表示オブジェクト
		QREditor qre;														//エディタオブジェクト
		ImageIcon icon = new ImageIcon("icon.png");							//アイコンの設定
	//メニューバー
		MenuItem NewFile	= new MenuItem( "新規作成" );					//[ファイル]->[新規作成]
		MenuItem Open		= new MenuItem( "開く" );						//[ファイル]->[開く]
		MenuItem Exit		= new MenuItem( "プログラムの終了" );			//[ファイル]->[プログラムの終了]
		MenuItem Editor		= new MenuItem( "エディタの起動" );				//[編集]->[エディタの起動]
		MenuItem Do		= new MenuItem( "変換" );						//[編集]->[変換]
		MenuItem ClearInput	= new MenuItem( "入力エリアのクリア" );			//[表示]->[入力エリアのクリア]
		MenuItem ClearInfo	= new MenuItem( "情報エリアのクリア" );			//[表示]->[情報エリアのクリア]
		MenuItem Version	= new MenuItem( "バージョン情報" );				//[ヘルプ]->[バージョン情報]
	//その他
		int WindowSizeX=450;												//ウィンドウサイズ(X)
		int WindowSizeY=770;												//ウィンドウサイズ(Y)
		float version=(float)0.7;											//プログラムのバージョンを格納
		static int simbolNumMax=999;										//シンボル表示オブジェクトの最高数
		boolean coop=false;													//グラフィックが埋め込まれているかどうか
		int count=0;														//一時的な変数
		String Title="QR-Code Maker";										//プログラムのタイトル
		String[] dataBit;													//データビット列を格納
		String Inputdata;													//入力されたデータを格納
		String[] InputdataDiv=new String[2];								//入力データをデータ部とグラフィック部に分けて格納
		String SelectedLv="L(7%)";											//選択された訂正レベルをテキストで格納
		String SelectedMode;												//選択されたエンコードモードをテキストで格納
		int verMax=40;														//最大型番
		static int ver=0;													//選択された型番を格納 0～40
		static int lv=0;													//選択された訂正レベルを格納 0～3
		static int encodeMode;												//エンコードモードを格納 0～3
		int mozisusizisiLen;												//文字数指示子の長さを格納
		int souCodeGosu=0;													//総コード語数を格納
		int errorTeiseiSu=0;												//エラー訂正数を格納
		String[][] errorF;													//誤り訂正符号を格納する配列
		int errorTS;														//誤り訂正
		int[] g;															//誤り訂正
		int[] f;															//誤り訂正
		String[] f2;														//誤り訂正
		String[] dtmp;														//誤り訂正
		String[][] dataB;													//インタリーブ配置の各データビット列を格納
		int RSSum=1;														//RSブロック数の合計
		int dBkazu;															//RSブロック数が複数の時にコード数の計算に使用
		int simbolNum=0;													//シンボルの生成番号
		public static int[][] simbol;										//シンボルを格納
		public static int simSize=0;										//シンボルのサイズを格納
		static int mask=0;													//適用するマスクの種類を格納
		int[][] currentSimbol;												//マスクの評価をする場合の各シンボルを格納
		int score;															//シンボルの評価の失点を格納
		String modesikibetusi="0000";										//モード識別子を格納
		String mozisusizisi=null;											//文字数指示子を格納
		static BufferedImage readImage=null;
		static int imageX=0;
		static int imageY=0;
                
                static int initImageSize=10;
                static int imageSize=0;

	////////////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////コンストラクタ////////////////////////////////////////
	QRCode(){
	//メニュー
		MenuBar MyMenu = new MenuBar();
		Menu FileMenu = new Menu( "ファイル" );
  		  	NewFile.addActionListener(this);
    		Open.addActionListener(this);
    		Exit.addActionListener(this);
			FileMenu.add( NewFile );
			FileMenu.add( Open );
			FileMenu.addSeparator();
			FileMenu.add( Exit );
		Menu EditMenu = new Menu( "編集" );
  		  	Editor.addActionListener(this);
  		  	Do.addActionListener(this);
			EditMenu.add(Editor);
			EditMenu.addSeparator();
			EditMenu.add(Do);
		Menu ShowMenu = new Menu( "表示" );
  		  	ClearInput.addActionListener(this);
  		  	ClearInfo.addActionListener(this);
			ShowMenu.add(ClearInput);
			ShowMenu.add(ClearInfo);
		Menu HelpMenu = new Menu( "ヘルプ" );
  		  	Version.addActionListener(this);
			HelpMenu.add(Version);
		MyMenu.add( FileMenu );
		MyMenu.add( EditMenu );
		MyMenu.add( ShowMenu );
		MyMenu.add( HelpMenu );
		setMenuBar(MyMenu);

	//ステータスバー
		StatusBar=new JLabel("Version:"+version);
	//ボタン
		b=new JButton("変換");
		b.addActionListener(this);
	//ボタン(開く)
		inB=new JButton("ファイルを開く");
		inB.addActionListener(this);
	//ボタン(開く)
		editorB=new JButton("エディタを開く");
		editorB.addActionListener(this);
	//ボタン(開く)
		clearB=new JButton("クリア");
		clearB.addActionListener(this);
        //テキストエリア
		//入力エリア
			inputD=new JTextArea("", 4,38);
			inputD.setLineWrap(true);
			inputDS=new JScrollPane(inputD);
			inputDS.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			inputDS.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			inputDS.setPreferredSize(new Dimension(WindowSizeX-25, 140));
		//情報エリア
			info=new JTextArea(Title+BR+BR, 4, 30);
			infoS=new JScrollPane(info);
			infoS.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			infoS.setPreferredSize(new Dimension(WindowSizeX-25, 200));
	//コンボボックス
		//訂正レベル
			lvList = new JComboBox();
			lvList.addItem(" L ( 7%)");
			lvList.addItem(" M (15%)");
			lvList.addItem(" Q (25%)");
			lvList.addItem(" H (30%)");
			lvList.setLightWeightPopupEnabled(false);	//重量コンポーネント化してプルダウンが表示されない不具合解消
			lvList.addActionListener(this);
		//型番
			verList = new JComboBox();
			verList.addItem("Auto");
			for(int i=1;i<=verMax;i++){
				verList.addItem(Integer.toString(i));
			}
			verList.setLightWeightPopupEnabled(false);	//重量コンポーネント化してプルダウンが表示されない不具合解消
			verList.addActionListener(this);
		//マスク
			maskList = new JComboBox();
			maskList.addItem("Auto");
			for(int i=0;i<8;i++){
				maskList.addItem(Integer.toString(i));
			}
			maskList.setLightWeightPopupEnabled(false);	//重量コンポーネント化してプルダウンが表示されない不具合解消
			maskList.addActionListener(this);
                //サイズ
                        sizeList = new JComboBox();
			sizeList.addItem("1");
			sizeList.addItem("2");
			sizeList.addItem("3");
			sizeList.addItem("4");
                        sizeList.addItem("5");
                        sizeList.addItem("6");
			sizeList.setLightWeightPopupEnabled(false);	//重量コンポーネント化してプルダウンが表示されない不具合解消
			sizeList.addActionListener(this);
	//レイアウト
		getContentPane().setLayout(new BoxLayout(getContentPane(),BoxLayout.Y_AXIS));
		getContentPane().setBackground(TITLE_GRAY);
		Panel p1,p2,p3,p4,p5,p6,p7,p8,p9,p10,p11;
		getContentPane().add(p1=new Panel());
		getContentPane().add(p2=new Panel());
		getContentPane().add(p3=new Panel());
		getContentPane().add(p4=new Panel());
		getContentPane().add(p5=new Panel());
		getContentPane().add(p7=new Panel());
		getContentPane().add(p9=new Panel());
		getContentPane().add(p10=new Panel());
		getContentPane().add(p11=new Panel());
		p1.setLayout(new FlowLayout());
		p1.add(new JLabel(new ImageIcon("title.gif")));
		p2.setLayout(new FlowLayout());
		p2.setBackground(TITLE_GRAY2);
		p2.add(inB);
		p2.add(editorB);
		p4.setLayout(new FlowLayout(FlowLayout.LEFT));
		p4.add(new JLabel("QRコード化するデータ"));
		p5.add(inputDS);
		p7.add(new JLabel("訂正"));
		p7.add(lvList);
		p7.add(new JLabel("　型番"));
		p7.add(verList);
		p7.add(new JLabel("　マスク"));
		p7.add(maskList);
                p7.add(new JLabel("　サイズ"));
		p7.add(sizeList);
		p7.add(new JLabel("　　"));
		p7.add(b);
                p7.add(clearB);
		p9.setLayout(new FlowLayout(FlowLayout.LEFT));
		p9.add(new JLabel("情報エリア"));
		p10.add(infoS);
		//p11.setBackground();
		p11.setLayout(new FlowLayout(FlowLayout.LEFT));
		p11.add(StatusBar,BorderLayout.SOUTH);
		setIconImage(icon.getImage());

		//終了処理
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//表示
			init();
			setTitle(Title);
			setSize(WindowSizeX, WindowSizeY);
			setVisible(true);
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////イベントメソッド////////////////////////////////////////
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		//ボタン(変換)のイベント
			if(obj==b || obj==Do){
				QRSyori();
				if(simbolNum<simbolNumMax){
					int qrsWidth=simSize*QRShow.blockWidth+(QRShow.quiet*2);
					qrs[simbolNum]=new QRShow(simbolNum,simbol,simSize,readImage,imageX,imageY);
					qrs[simbolNum].setTitle(""+ver+SelectedLv.charAt(0)+"型 ："+inputD.getText());
					qrs[simbolNum].setLocation(getX()+(getWidth()/2)-(qrsWidth/2), getY()+(getHeight()/2)-(qrsWidth/2));
					simbolNum++;
					ver=verList.getSelectedIndex();
					//readImage=null;
				}else{
					System.out.println("エラー：　これ以上シンボルを生成できません。");
					info.setText(info.getText()+"エラー：　これ以上シンボルを生成できません。"+BR);
				}
			}
		//ボタン(エディタを開く)のイベント
			if(obj==editorB || obj==Editor){
				qre=new QREditor();
				qre.setLocation(getX()+getWidth(), getY());
			}
                /*//ボタン(クリア)のイベント
                        if(obj==clearB || obj==Clear){
				inputD.setEditable(true);
                                inputD.setText("");
                                readImage = null;
			}*/
		//ボタン(開く)のイベント
			if(obj==inB || obj==Open){
				int returnVal = chooser.showOpenDialog(this);
				try {
					if (returnVal == JFileChooser.APPROVE_OPTION) {	// OKボタンが押された時の処理
						File file = chooser.getSelectedFile();
						BufferedReader in  = new BufferedReader(new FileReader(file));
						boolean ioError=false;
						int simSizeTmp=0;
						int[][] simbolTmp=new int[simSizeTmp][simSizeTmp];
						String str1=null;
						String str2=null;
						//型番、モード、訂正レベル、マスク取得
							String[] ioData;
							ioData=in.readLine().split(",");
							if(ioData.length!=4){
								ioError=true;
							}
						simError:
						if(!ioError){
							//シンボル取得
								simSizeTmp = 21+(4*(Integer.parseInt(ioData[0])-1));
								simbolTmp=new int[simSizeTmp][simSizeTmp];
								String[] ioData2;
								for(int i=0;i<simSizeTmp;i++){
									ioData2=in.readLine().split(",");
									if(ioData2.length!=simSizeTmp){
										ioError=true;
										break simError;
									}
								}
							//入力データ、出力データ取得
								str1=in.readLine();
								str2=in.readLine();
								if(str2.indexOf(str1+GRAP_SEPARATOR)==-1){
									ioError=true;
								}
						}
						//エラー処理
							if(ioError){
								DataBase.ioError();
								JOptionPane.showMessageDialog(this, "ファイルの読み込みに失敗しました。","エラー", JOptionPane.ERROR_MESSAGE);
							}else{
								ver=Integer.parseInt(ioData[0]);
								encodeMode=Integer.parseInt(ioData[1]);
								lv=Integer.parseInt(ioData[2]);
								mask=Integer.parseInt(ioData[3]);
								verList.setSelectedIndex(ver);
								lvList.setSelectedIndex(lv);
								maskList.setSelectedIndex(mask+1);
								str2=str2.replaceAll(BR_IO,BR);
								inputD.setText(str2);
							}
						in.close();
					}
				} catch(Exception ex){}
			}
		//コンボボックス(型番)のイベント
			if(obj==verList){
				ver=verList.getSelectedIndex();
			}
		//コンボボックス(マスク)のイベント
			if(obj==maskList){
				mask=maskList.getSelectedIndex();
			}
		//コンボボックス(訂正レベル)のイベント
			if(obj==lvList){
				lv=lvList.getSelectedIndex();
				switch(lv){
					case 0:
						SelectedLv="L(7%)";
						break;
					case 1:
						SelectedLv="M(15%)";
						break;
					case 2:
						SelectedLv="Q(25%)";
						break;
					case 3:
						SelectedLv="H(30%)";
				}
			}
                        if(obj==sizeList){
				QRShow.blockWidth=sizeList.getSelectedIndex()+4;
                                QRShow.quiet=QRShow.blockWidth*5;
			}
		//新規作成のイベント
			if(obj==NewFile){
				init();
			}
		//プログラムの終了のイベント
			if(obj==Exit){
				System.exit(0);
			}
		//情報エリアのクリアイベント
			if(obj==clearB || obj==ClearInput){
				inputD.setEditable(true);
                                inputD.setText("");
                                readImage = null;
			}
		//情報エリアのクリアイベント
			if(obj==ClearInfo){
				info.setText("");
			}
		//バージョン情報のイベント
			if(obj==Version){
				JOptionPane.showMessageDialog(this, "バージョン:"+version,"バージョン情報", JOptionPane.PLAIN_MESSAGE);
			}
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////メインメソッド////////////////////////////////////////
	public static void main(String[] args) {
		QRCode qr=new QRCode();
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////QRコード計算処理メソッド///////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	public void QRSyori(){
		Inputdata=inputD.getText();
		/////////////////////////////////////////////////////////////
		///////////////グラフィックが埋め込まれているか//////////////
		int GrapIndex=Inputdata.lastIndexOf(GRAP_SEPARATOR);
			if(GrapIndex==-1){
				//グラフィックデータがない時の処理
				coop=false;
				InputdataDiv[0]=Inputdata;
			}else{
				//グラフィックデータがある時の処理
				coop=true;
				encodeMode=2;
				InputdataDiv[0]=Inputdata.substring(0,GrapIndex);
				InputdataDiv[1]=Inputdata.substring(GrapIndex+GRAP_SEPARATOR.length());
		}

		//////////////////////////////////////////////////////////////
		//////////////////////////モード決定//////////////////////////
			encodeMode=DataBase.getEncodeMode(InputdataDiv[0]);
			switch(encodeMode){
				case 0:
					SelectedMode="数字";
					break;
				case 1:
					SelectedMode="英数字";
					break;
				case 2:
					SelectedMode="8bitバイト";
					break;
				case 3:
					SelectedMode="漢字";
					break;
				default:
					//エラー
					SelectedMode="不明";
					System.out.println("エラー：　エンコードモードの値が異常です。　："+encodeMode);
					info.setText(info.getText()+"エラー：　エンコードモードの値が異常です。　："+encodeMode+BR);
			}
			//エディタで自動でモードを決定できるようになれば↓削除
				/*if(coop){
					encodeMode=2;
				}
				*/
		/////////////////////////////////////////////////////////////
		////////////////決定されたモードの型番別容量を取得///////////
		for(int i=1;i<=40;i++){
			mozisusizisiLen=DataBase.mozisusizisiLenGet(i,encodeMode);
				switch(encodeMode){
				case 0:
					versionMaxLen[i][lv][encodeMode]=(int)((versionMaxLen[i][lv][4]*8) -(4+mozisusizisiLen))/10*3;
					if(((versionMaxLen[i][lv][4]*8) -(4+mozisusizisiLen))%10>6){
						versionMaxLen[i][lv][encodeMode]+=2;
					}else if(((versionMaxLen[i][lv][4]*8) -(4+mozisusizisiLen))%10>3){
						versionMaxLen[i][lv][encodeMode]+=1;
					}
					break;
				case 1:
					versionMaxLen[i][lv][encodeMode]=(int)((versionMaxLen[i][lv][4]*8) -(4+mozisusizisiLen))/11*2+(int)((((versionMaxLen[i][lv][4]*8) -(4+mozisusizisiLen))%11+4)/10.0);
					break;
				case 2:
					versionMaxLen[i][lv][encodeMode]=(int)((versionMaxLen[i][lv][4]*8) -(4+mozisusizisiLen))/8;
					break;
				case 3:

			}
		}

		//////////////////////////////////////////////////////////////
		//////////////////////バージョン決定が自動の時/////////////////
		if(ver==0){
			for(int i=1;i<41;i++){
				if(Inputdata.length()<=versionMaxLen[i][lv][encodeMode]){
					ver=i;
					break;
				}
			}
		}
		//デバッグ用
			System.out.println("入力文字数="+Inputdata.length()+"、型番"+ver+"の許容文字数="+versionMaxLen[ver][lv][encodeMode]);

		//////////////////////////////////////////////////////////////
		//////////////////////////文字数指示子//////////////////////////
		mozisusizisiLen=DataBase.mozisusizisiLenGet(ver,encodeMode);
		mozisusizisi = DataBase.get2(InputdataDiv[0].length(), mozisusizisiLen);

		//////////////////////////////////////////////////////////////
		//////////////////////////モード指示子を取得////////////////////
		modesikibetusi=DataBase.getMode(encodeMode);

		//////////////////////////////////////////////////////////////
		//////////////////////////データの符号化//////////////////////
		InputdataDiv[0]=DataBase.DtoB(InputdataDiv[0],encodeMode);

		//////////////////////////////////////////////////////////////
		////////////////////////終端パターン挿入///////////////////////
		String tmpS=null;
		int terminatorLen=0;
		tmpS = modesikibetusi+mozisusizisi+InputdataDiv[0];
		//デバッグ用
			System.out.println("データの量="+tmpS.length()+"　限界量="+((versionMaxLen[ver][lv][4]*8)));
		String terminator="";
		terminatorLen=(versionMaxLen[ver][lv][4]*8)-tmpS.length();
		if(terminatorLen>3){
			terminator = "0000";
		}else{
			for(int i=0;i<terminatorLen;i++){
				terminator+="0";
				//デバッグ用
					System.out.println("終端パターン挿入数="+terminatorLen);
			}
		}
		tmpS +=terminator;
		////////////////////////////////////////////////////////////////
		/////////////////8bitずつに区切り、埋め草bit挿入/////////////////
		//データビットを格納する配列の生成
			dataBit=new String[versionMaxLen[ver][lv][4]+versionMaxLen[ver][lv][5]];
		int nowPlace=0;
		//グラフィックがあればデータを代入
			if(coop){
				tmpS+=InputdataDiv[1];
			}
		//8bit単位でdataBit配列にデータを格納
			for (int i=0; tmpS.length()>0; i++) {
				if(tmpS.length()<8){
					count=tmpS.length();
				}else{
					count=8;
				}
				dataBit[nowPlace] = tmpS.substring(0, count);
				tmpS = tmpS.substring(count);
				nowPlace++;
			}
		if(!coop){
			//最後のbit列が8bitになるよう埋め草bit挿入(グラフィックない場合)
				for (int j=0; dataBit[nowPlace-1].length()<8; j++) {
						dataBit[nowPlace-1] += "0";
				}
			//埋め草bit列をデータコード数を満たすまで挿入(グラフィックない場合)
				count = 0;
				for (; ; nowPlace++) {
					if (nowPlace<versionMaxLen[ver][lv][4]) {
						if (count%2==1) {
							dataBit[nowPlace] = "00010001";
						} else {
							//dataBit[nowPlace] = "11101100";
							dataBit[nowPlace] = "11101101";
						}
						count++;
					} else {
						break;
					}
				}
		}
		//デバッグ用
			System.out.println("[データビット列の表示]");
			for (int j=0; j<versionMaxLen[ver][lv][4]; j++) {
				System.out.println("dataBit["+j+"] ="+dataBit[j]+"=>"+DataBase.get10(dataBit[j]));
			}

		//データを10進数に変換
			for (int i=0; i<versionMaxLen[ver][lv][4]; i++) {
				dataBit[i] = Integer.toString(DataBase.get10(dataBit[i]));
			}

		//////////////////////////////////////////////////////////////
		////////////////////////誤り訂正//////////////////////////////
		//総コード語数を計算
			souCodeGosu = versionMaxLen[ver][lv][4]+versionMaxLen[ver][lv][5];
		//RSブロック数の合計を計算
			RSSum=versionMaxLen[ver][lv][6]+versionMaxLen[ver][lv][7];
		//エラー訂正数を計算
			errorTeiseiSu = versionMaxLen[ver][lv][5]/2;
			if (ver == 1 && lv == 0) {
				//1Lの時は2
				errorTeiseiSu = 2;
			} else if (ver == 1 && lv == 1) {
				//1Mの時は4
				errorTeiseiSu = 4;
			} else if (ver == 2 && lv == 0) {
				//2Lの時は4
				errorTeiseiSu = 4;
			} else if (ver == 1 && lv == 3) {
				//1Hの時は8
				errorTeiseiSu = 8;
			}
		//各ブロックの誤り訂正コード数を取得
			errorTS=versionMaxLen[ver][lv][5];
			errorTS=errorTS/RSSum;
		//生成多項式gを取得
			G(errorTS);
		//RSブロック数1の時の誤り訂正
			if (RSSum == 1) {
				errorF=new String[1][errorTS];
				errorCode(dataBit,errorTS,errorF[0],versionMaxLen[ver][lv][4]);	
				//データを配置する順にソート
					for(int i=0;i<errorF[0].length;i++){
						dataBit[versionMaxLen[ver][lv][4]+i]=errorF[0][i];
					}
				//デバッグ用
					for(int i=0;i<dataBit.length;i++){
						//System.out.println("dataBit["+i+"]= "+dataBit[i]);
					}
			}
		//RSブロック数が2以上の時の誤り訂正
			if(RSSum>1){
				//デバッグ用
					System.out.println("RSブロック数2以上");
				//データブロックの配列を生成
					 dataB=new String[RSSum][];
				//１つめのRSブロック数
					count=0;
					for(int i=0;i<versionMaxLen[ver][lv][6];i++){
						dBkazu=(int)(versionMaxLen[ver][lv][4]/RSSum);
						dataB[i]=new String[dBkazu];
						for(int j=0;j<dBkazu;j++){
							dataB[i][j]=dataBit[count];
							count++;
						//デバッグ用
								System.out.println("dataB["+i+"]["+j+"]="+dataB[i][j]+"　→　"+DataBase.get2(Integer.parseInt(dataB[i][j]),10));
						}
					}
				//2つめのRSブロック数
					for(int i=0;i<versionMaxLen[ver][lv][7];i++){
						dBkazu=(int)(versionMaxLen[ver][lv][4]/RSSum)+1;
						dataB[i+versionMaxLen[ver][lv][6]]=new String[dBkazu];
						for(int j=0;j<dBkazu;j++){
							dataB[i+versionMaxLen[ver][lv][6]][j]=dataBit[count];
							count++;
							//デバッグ用
								System.out.println("dataB["+(i+versionMaxLen[ver][lv][6])+"]["+j+"]="+dataB[i+versionMaxLen[ver][lv][6]][j]+"　→　"+DataBase.get2(Integer.parseInt(dataB[i+versionMaxLen[ver][lv][6]][j]),10));
						}
					}

				//それぞれのデータブロックの誤り訂正符号を計算
					//誤り訂正符号を格納する配列の生成
						errorF=new String[RSSum][errorTS];
					for(int i=0;i<RSSum;i++){
						errorCode(dataB[i],errorTS,errorF[i],dataB[i].length);
					}
				//データを配置する順にソート
					dataBit=new String[versionMaxLen[ver][lv][4]+errorTS*RSSum];
				//データコード部分をソート
					count=0;
					for(int i=0;i<Math.floor(versionMaxLen[ver][lv][4]/RSSum);i++){
						for(int j=0;j<dataB.length;j++){
							dataBit[count]=dataB[j][i];
							count++;
						}
					}
					for(int i=versionMaxLen[ver][lv][6];i<RSSum;i++){
						dataBit[count]=dataB[i][((int)(versionMaxLen[ver][lv][4]/RSSum))];
						count++;
					}
				//誤り訂正符号部分をソート
					for(int i=0;i<errorTS;i++){
						for(int j=0;j<RSSum;j++){
							dataBit[count]=errorF[j][i];
							count++;
						}
					}
			}

			//デバッグ用
				for(int i=0;i<dataBit.length;i++){
					//System.out.println("最終dataBit["+i+"]= "+dataBit[i]);
				}
		//////////////////////////////////////////////////////////////
		//////////////////////データの２進化//////////////////////////
			for (int i=0; i<dataBit.length; i++) {
				dataBit[i]=DataBase.get2(Integer.parseInt(dataBit[i]),8);
			}
	////////////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////QRコード表示処理//////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////

		//////////////////////////////////////////////////////////////
		/////////////////シンボルの生成、初期設定///////////////////////
		//シンボルを生成
			simSize = 21+(4*(ver-1));
			simbol = new int[simSize][simSize];
		//シンボルに固定データの埋め込み
			DataBase.Indata(ver,simSize,simbol);

		//////////////////////////////////////////////////////////////
		///////////////////シンボルにデータの配置/////////////////////
		//初期設定
			int direct=1;
		  	int LeftOrRight=1;
		  	int nowX=simSize-1;
		  	int nowY=simSize-1;
			int haitiBitLength;
		  	int[] haitiBit=new int[dataBit.length*8];
			count=0;
			for(int i=0;i<dataBit.length;i++){
				for(int j=0;j<dataBit[i].length();j++){
					haitiBit[count]=Character.digit(dataBit[i].charAt(j),10);
					count++;
				}
			}
		  //配置
			haitiBitLength=haitiBit.length;
			int compNum=0;
			for(int i=0;compNum<haitiBit.length;i++){
				count=0;
				//デバッグ用
					if(nowX<-1){
						System.out.println("エラー：　データがシンボルに収まりきりません　　残りビット数："+(haitiBit.length-i-1));
						info.setText(info.getText()+"エラー：　データがシンボルに収まりきりません　　残りビット数："+(haitiBit.length-i-1)+BR);
					}
				if(nowX>=0 && nowX<=simSize-1 && nowY>=0 && nowY<=simSize-1){
					if(simbol[nowY][nowX]==0){
						//データが空ならビットを配置
							if(haitiBit[compNum]==1){
								simbol[nowY][nowX]=DATA_1;
							}else if(haitiBit[compNum]==0){
							 	simbol[nowY][nowX]=DATA_0;
							}
							count=1;
							compNum++;
					}
				}

				if(count==0){
					//移動
						if(LeftOrRight==1){
							//右側
							//デバッグ用
								//System.out.println("右側		"+nowX+",	"+nowY);
							nowX--;
							LeftOrRight=-LeftOrRight;
						}else if(nowY>=0 && nowY<=simSize-1){
							//デバッグ用
								//System.out.println("上下移動可能	"+nowX+",	"+nowY);
							nowY-=direct;
							nowX++;
							LeftOrRight=-LeftOrRight;
						}else{
							//デバッグ用
								//System.out.println("上下限界	"+nowX+",	"+nowY);
							nowX--;
							if(nowX==6){
								nowX--;
							}
							LeftOrRight=-LeftOrRight;
							nowY+=direct;
							direct=-direct;	
						}						   
			 	}
			}

		//////////////////////////////////////////////////////////////
		//////////////////////////マスク処理///////////////////////////
		int Maxscore=99999;
		currentSimbol=new int[simSize][];
		//各マスクの評価
			if(mask==0){
				for(int i=0;i<8;i++){
					//シンボルのコピー
						for(int j=0;j<simSize;j++){
							currentSimbol[j]=(int[])simbol[j].clone();
						}
					//マスクの適用
						DataBase.mask(currentSimbol,i,simSize,0);
					//形式情報の埋め込み
						DataBase.setFormatInformation(currentSimbol,lv,i);
					//シンボルの評価
						System.out.print("Mask;"+i+"　");
						score=CheckSimbol(currentSimbol);
					//マスクの決定
						if(Maxscore>score){
							mask=i;
							Maxscore=score;
						}
				}
			}else{
				mask-=1;
				score=CheckSimbol(simbol);
			}
		//シンボルの決定
				System.out.println("採用されたマスク="+mask);
			//マスクの適用
				if(coop){
					DataBase.mask(simbol,mask,simSize,0);
				}else{
					DataBase.mask(simbol,mask,simSize,0);
				}
			//形式情報の埋め込み
				DataBase.setFormatInformation(simbol,lv,mask);
		//デバッグ用
			//DataBase.sTrace(simbol,simSize,"データ配置");

		//終了
			info.setText(info.getText()+"型番="+ver+"　訂正レベル="+SelectedLv+"　マスク="+mask+"　失点="+score+"　エンコードモード="+SelectedMode+BR);
			info.setText(info.getText()+"QR化処理終了しました。"+BR+"========================================"+BR);
			mask=maskList.getSelectedIndex();
	}


	/////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////メソッド//////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////

	//////////////////////////////////////////////////////////////
	////////////GF(256)のαを整数に変換するメソッド///////////////
	public int ahenkan(int a){
		if(a<1 || a>255){
			return 0;
		}
		return dataA[a-1];	
	}

	//////////////////////////////////////////////////////////////
	////////////GF(256)の整数をαに変換するメソッド///////////////
	public int agyakuhenkan(int a){
		for(int iii=0;iii<255;iii++){
			if(a==dataA[iii]){
				return iii+1;
			}else if(a==255){
				return 1;
			}
		}
		System.out.println("エラー：　agyakuhenkan()にて"+a);
		info.setText(info.getText()+"エラー：　agyakuhenkan()にて変換できない数字が入力されました。( 1～255) a="+a+BR); 
		return 0;
	}
	//////////////////////////////////////////////////////////////
	///////////////////べき乗を計算するメソッド///////////////////
	public int beki(int a,int b){
		int c=1;
		for(int iii=0;iii<b;iii++){
			c*=a;
		}
		return c;
	}

	//////////////////////////////////////////////////////////////
	///////////////////生成多項式を求めるメソッド/////////////////
	public int[] G(int a){
		//生成多項式g(x)を求める
			switch(a){
				case 7:
					int[] g1 = {0, 87, 229, 146, 149,238,102,21};
					g=(int[])g1.clone();
					break;
				case 10:
					int[] g2 = {0,251,67,46,61,118,70,64,94,32,45};
					g=(int[])g2.clone();
					break;
				case 13:
					int[] g3 = {0,74,152,176,100,86,100,106,104,130,218,206,140,78};
					g=(int[])g3.clone();
					break;
				case 15:
					int[] g4 = {0,8,183,61,91,202,37,51,58,58,237,140,124,5,99,105};
					g=(int[])g4.clone();
					break;
				case 16:
					int[] g5 = {0,120,104,107,109,102,161,76,3,91,191,147,169,182,194,225,120};
					g=(int[])g5.clone();
					break;
				case 17:
					int[] g6 = {0, 43, 139, 206, 78, 43, 239, 123, 206, 214, 147, 24, 99, 150, 39, 243, 163, 136};
					g=(int[])g6.clone();
					break;
				case 18:
					int[] g7 = {0,215,234,158,94,184,97,118,170,79,187,152,148,252,179,5,98,96,153};
					g=(int[])g7.clone();
					break;
				case 20:
					int[] g8 = {0,17,60,79,50,61,163,26,187,202,180,221,225,83,239,156,164,212,212,188,190};
					g=(int[])g8.clone();
					break;
				case 22:
					int[] g9 = {0,210,171,247,242,93,230,14,109,221,53,200,74,8,172,98,80,219,134,160,105,165,231};
					g=(int[])g9.clone();
					break;
				case 24:
					int[] g10 = {0,229,121,135,48,211,117,251,126,159,180,169,152,192,226,228,218,111,0,117,232,87,96,227,21};
					g=(int[])g10.clone();
					break;
				case 26:
					int[] g11 = {0,173,125,158,2,103,182,118,17,145,201,111,28,165,53,161,21,245,142,13,102,48,227,153,145,218,70};
					g=(int[])g11.clone();
					break;
				case 28:
					int[] g12 = {0,168,223,200,104,224,234,108,180,110,190,195,147,205,27,232,201,21,43,245,87,42,195,212,119,242,37,9,123};
					g=(int[])g12.clone();
					break;
				case 30:
					int[] g13 = {0,41,173,145,152,216,31,179,182,50,48,110,86,239,96,222,125,42,173,226,193,224,130,156,37,251,216,238,40,192,180};
					g=(int[])g13.clone();
					break;
				case 32:
					int[] g14 = {0,10,6,106,190,249,167,4,67,209,138,138,32,242,123,89,27,120,185,80,156,38,60,171,60,28,222,80,52,254,185,220,241};
					g=(int[])g14.clone();
					break;
				case 34:
					int[] g15 = {0,111,77,146,94,26,21,108,19,105,94,113,193,86,140,163,125,58,158,229,239,218,103,56,70,114,61,183,129,167,13,98,62,129,51};
					g=(int[])g15.clone();
					break;
				case 36:
					int[] g16 = {0,200,183,98,16,172,31,246,234,60,152,115,24,167,152,113,248,238,107,18,63,218,37,87,210,105,177,120,74,121,196,117,251,113,233,30,120};
					g=(int[])g16.clone();
					break;
				case 40:
					int[] g17 = {0,59,116,79,161,252,98,128,205,128,161,247,57,163,56,235,106,53,26,187,174,226,104,170,7,175,35,181,114,88,41,47,163,125,134,72,20,232,53,35,15};
					g=(int[])g17.clone();
					break;
				case 42:
					int[] g18 = {0,42,250,103,221,230,25,18,137,231,0,3,58,242,221,191,110,84,230,8,188,106,96,147,15,131,139,34,101,223,39,101,213,199,237,254,201,123,171,162,194,117,50,96};
					g=(int[])g18.clone();
					break;
				case 44:
					int[] g19 = {0,190,7,61,121,71,246,69,55,168,188,89,243,191,25,72,123,9,145,14,247,1,238,44,78,143,62,224,126,118,114,68,163,52,194,217,147,204,169,37,130,113,102,73,181};
					g=(int[])g19.clone();
					break;
				case 46:
					int[] g20 = {0,112,94,88,112,253,224,202,115,187,99,89,5,54,113,129,44,58,16,135,216,169,211,36,1,4,96,60,241,73,104,234,8,249,245,119,174,52,25,157,224,43,202,223,19,82,15};
					g=(int[])g20.clone();
					break;
				case 48:
					int[] g21 = {0,228,25,196,130,211,146,60,24,251,90,39,102,240,61,178,63,46,123,115,18,221,111,135,160,182,205,107,206,95,150,120,184,91,21,247,156,140,238,191,11,94,227,84,50,163,39,34,108};
					g=(int[])g21.clone();
					break;
				case 50:
					int[] g22 = {0,232,125,157,161,164,9,118,46,209,99,203,193,35,3,209,111,195,242,203,225,46,13,32,160,126,209,130,160,242,215,242,75,77,42,189,32,113,65,124,69,228,114,235,175,124,170,215,232,133,205};
					g=(int[])g22.clone();
					break;
				case 52:
					int[] g23 = {0,116,50,86,186,50,220,251,89,192,46,86,127,124,19,184,233,151,215,22,14,59,145,37,242,203,134,254,89,190,94,59,65,124,113,100,233,235,121,22,76,86,97,39,242,200,220,101,33,239,254,116,51};
					g=(int[])g23.clone();
					break;
				/*
				case 54:
					int[] g24 = {};
					g=(int[])g24.clone();
					break;
				case 56:
					int[] g25 = {};
					g=(int[])g25.clone();
					break;
				case 58:
					int[] g26 = {};
					g=(int[])g26.clone();
					break;
				case 60:
					int[] g27 = {};
					g=(int[])g27.clone();
					break;
				*/
				//
				//
				//他の部分の定義も行う
				//
				//	
				default:
					System.out.println("エラー：　誤り訂正コード語数が異常で、生成多項式g(x)が作れません　："+a);
					info.setText(info.getText()+"エラー：　誤り訂正コード語数が異常で、生成多項式g(x)が作れません　："+a+BR);
			}
		return g;
	}

	//////////////////////////////////////////////////////////////
	////////////////誤り訂正符号を計算するメソッド////////////////
	public void	errorCode (String a[],int b,String c[],int d){

		//各配列を生成、初期化
			f = new int[d+errorTS];
			f2 =new String[d+errorTS];
		//各配列に初期値0を入れる
			for (int i=0; i<d+errorTS; i++) {
				f2[i]="0";
				f[i]=0;
			}
		//f2配列にdataBitをコピー
			for (int i=0; i<d; i++) {
				f2[i] = a[i];
			}
			//デバッグ用
			/*
				for(int i=0;i<f2.length;i++){
					System.out.println("f2["+i+"]="+f2[i]);
				}
			*/
		//誤り訂正符号の計算
			count=0;
			for (int j=0; j<d; j++) {
				count = Integer.parseInt(f2[j]);
				for (int i=j; i<j+1+errorTS; i++) {
					f[i] = g[i-j]+ahenkan(count);
					if (f[i]>255) {
						f[i] -= 255;
					}
					f[i] = agyakuhenkan(f[i]);
					f2[i] = DataBase.exor(DataBase.get2(f[i], 1), DataBase.get2(Integer.parseInt(f2[i]), 1));
					f2[i] = Integer.toString(DataBase.get10(f2[i]));
				}
			}
		//誤り訂正符号を配列に格納
			for (int i=0; i<b; i++) {
				c[i] = f2[d+i];
			}
		//デバッグ用
			/*
			for(int i=0;i<f2.length;i++){
				System.out.println("f2["+i+"]="+f2[i]);
			}
			System.out.println("===========================================");
			for(int i=0;i<f.length;i++){
				System.out.println("f["+i+"]="+f[i]);
			}
			System.out.println("[誤り訂正符号を表示]");
			for (int i=0; i<b; i++) {
				System.out.println("c["+i+"] ="+c[i]);
			}
			*/
	}

	///////////////////////////////////////////////////////////////////
	////////////////////シンボルの評価をするメソッド///////////////////	
	public int CheckSimbol(int a[][]){
	int score=0;
	int countX=0;
	int countY=0;
	boolean colorX=false;
	boolean colorY=false;
	//シンボルを黒と白にした配列を用意
		boolean[][] b=new boolean[simSize][simSize];
		b=simbolToBoolean(a);
		//デバッグ用
			//DataBase.sTrace(b,simSize,"デバッグ用");
		//同色の行・列の隣接モジュール
			//行
			for(int i=0;i<simSize;i++){
				colorX=b[0][i];
				countX=1;
				for(int j=1;j<simSize;j++){
					if(b[j][i]==colorX && j<simSize-1){
						countX++;
					}else{
						if(b[j][i]==colorX){
							countX++;
						}
						if(countX>=5){
							//System.out.println("同色の行の隣接モジュール s["+i+"]["+(j-countX)+"]-s["+i+"]["+(j-1)+"]：-"+(countX-2));
							score+=countX-2;
						}
						countX=1;
						colorX=b[j][i];
					}
				}
			}
			//列
			for(int i=0;i<simSize;i++){
				colorY=b[i][0];
				countY=1;
				for(int j=1;j<simSize;j++){
					if(b[i][j]==colorY && j<simSize-1){
						countY++;
					}else{
						if(b[i][j]==colorY){
							countY++;
						}
						if(countY>=5){
							//System.out.println("同色の列の隣接モジュール s["+(j-countY)+"]["+i+"]-s["+(j-1)+"]["+i+"]：-"+(countY-2));
							score+=countY-2;
						}
						countY=1;
						colorY=b[i][j];
					}
				}
			}
		//同色のモジュールブロック数
			for(int i=0;i<simSize-1;i++){
				for(int j=0;j<simSize-1;j++){
					if(b[i][j]==b[i+1][j] && b[i][j]==b[i][j+1] && b[i][j]==b[i+1][j+1]){
							//System.out.println("同色のモジュールブロック s["+j+"]["+i+"]-s["+(j+1)+"]["+(i+1)+"]：-3");
							score+=3;
					}
				}
			}
		//全体に占める暗部分の割合
			count=0;
			for(int i=0;i<simSize;i++){
				for(int j=0;j<simSize;j++){
					if(b[i][j]){
						count++;
					}
				}
			}
			//デバッグ用
				//System.out.println("全体="+(simSize*simSize)+"　黒="+count);
			float hi,hi2;
			hi=(float)count/(simSize*simSize)*100;
			hi2=Math.abs(hi-50);
			//System.out.println("全体に占める暗部分の割合="+(int)hi+"%：-"+((int)(hi2/5)*10+10));
			score+=(int)(hi2/5)*10+10;
		//行・列における1:1:3:1:1比率のパターン
		int hi3=0;
		int count=0;
		int dankai=0;
		int hit;
		int Save=0;
		int nowX=0,nowY=0;
		//列
		for(int i=0;i<simSize;i++){
			hi3=0;
			count=0;
			dankai=0;
			hit=0;
			for(int j=0;j<simSize;j++){
				if(dankai==0){
					if(b[j][i]){
						hi3++;
					}else{
						if(hi3>0){
							nowX=j-hi3;
							nowY=i;
							dankai++;
						}
					}
				}
				if(dankai==1){
					//System.out.println("段階1　比率="+hi3+"　s["+nowY+"]["+nowX+"]");
					if(!b[j][i]){
						count++;
					}else{
						if(count==hi3){
							dankai++;
							Save=j;
						}else{
							dankai=0;
							hi3=1;
						}
						count=0;
					}
				}
				if(dankai==2){
					//System.out.println("段階2　比率="+hi3+"　s["+nowY+"]["+nowX+"]");
					if(b[j][i]){
						count++;
					}else{
						if(count==(hi3*3)){
							dankai++;
						}else{
							dankai=0;
							j=Save;
							hi3=1;
						}
						count=0;
					}
				}
				if(dankai==3){
					//System.out.println("段階3　比率="+hi3+"　s["+nowY+"]["+nowX+"]");
					if(!b[j][i]){
						count++;
					}else{
						if(count==hi3){
							dankai++;
						}else{
							dankai=0;
							j=Save;
							hi3=1;
						}
						count=0;
					}
				}
				if(dankai==4){
					//System.out.println("段階4　比率="+hi3+"　s["+nowY+"]["+nowX+"]");
					if(b[j][i]){
						count++;
						if(j==simSize-1){
							hit=1;
							dankai++;
						}
					}else{
						if(count==hi3){
							dankai++;
						}else{
							dankai=0;
							hi3=0;							
						}
						count=0;
					}
				}
				if(dankai==5){
					//System.out.println("段階5　比率="+hi3+"　s["+nowY+"]["+nowX+"]");
					loop1:
					for(int ii=j;ii<j+4;ii++){
						if(hit==1){
							break loop1;
						}
						if(ii<simSize){
							if(b[ii][i]){
								break loop1;
							}
							if(ii==j+3){
								hit=1;
							}
						}else{
							hit=1;
							break loop1;
						}
					}
					loop2:
					for(int ii=nowX-1;ii>nowX-5;ii--){
						if(hit==1){
							break loop2;
						}
						if(ii>-1){
							if(b[ii][i]){
								dankai=0;
								break loop2;
							}
							if(ii==nowX-4){
								hit=1;
							}
						}else{
							hit=1;
							break loop2;
						}
					}
					if(hit==1){
						//System.out.println("1:1:3:1:1の比率　s["+nowY+"]["+nowX+"]：-30");
						score+=30;
						hit=0;
						dankai=0;
						hi3=0;
					}
				}
			}
		}
		//行
		for(int i=0;i<simSize;i++){
			hi3=0;
			count=0;
			dankai=0;
			hit=0;
			for(int j=0;j<simSize;j++){
				if(dankai==0){
					if(b[i][j]){
						hi3++;
					}else{
						if(hi3>0){
							nowX=i;
							nowY=j-hi3;
							dankai++;
						}
					}
				}
				if(dankai==1){
					//System.out.println("段階1　比率="+hi3+"　s["+nowY+"]["+nowX+"]");
					if(!b[i][j]){
						count++;
					}else{
						if(count==hi3){
							dankai++;
							Save=j;
						}else{
							dankai=0;
							hi3=1;
						}
						count=0;
					}
				}
				if(dankai==2){
					//System.out.println("段階2　比率="+hi3+"　s["+nowY+"]["+nowX+"]");
					if(b[i][j]){
						count++;
					}else{
						if(count==(hi3*3)){
							dankai++;
						}else{
							dankai=0;
							j=Save;
							hi3=1;
						}
						count=0;
					}
				}
				if(dankai==3){
					//System.out.println("段階3　比率="+hi3+"　s["+nowY+"]["+nowX+"]");
					if(!b[i][j]){
						count++;
					}else{
						if(count==hi3){
							dankai++;
						}else{
							dankai=0;
							j=Save;
							hi3=1;
						}
						count=0;
					}
				}
				if(dankai==4){
					//System.out.println("段階4　比率="+hi3+"　s["+nowY+"]["+nowX+"]");
					if(b[i][j]){
						count++;
						if(j==simSize-1){
							hit=1;
							dankai++;
						}
					}else{
						if(count==hi3){
							dankai++;
						}else{
							dankai=0;
							hi3=0;							
						}
						count=0;
					}
				}
				if(dankai==5){
					//System.out.println("段階5　比率="+hi3+"　s["+nowY+"]["+nowX+"]");
					loop3:
					for(int ii=i;ii<i+4;ii++){
						if(hit==1){
							break loop3;
						}
						if(ii<simSize){
							if(b[ii][j]){
								break loop3;
							}
							if(ii==i+3){
								hit=1;
							}
						}else{
							hit=1;
							break loop3;
						}
					}
					loop4:
					for(int ii=nowY-1;ii>nowY-5;ii--){
						if(hit==1){
							break loop4;
						}
						if(ii>-1){
							if(b[ii][j]){
								dankai=0;
								break loop4;
							}
							if(ii==nowY-4){
								hit=1;
							}
						}else{
							hit=1;
							break loop4;
						}
					}
					if(hit==1){
						//System.out.println("1:1:3:1:1の比率　s["+nowY+"]["+nowX+"]：-30");
						score+=30;
						hit=0;
						dankai=0;
						hi3=0;
					}
				}
			}
		}




	System.out.println("失点="+score);
	return score;
	}

	///////////////////////////////////////////////////////////////////
	////////////シンボルをintからbooleanに変換するメソッド/////////////
	public boolean[][] simbolToBoolean(int[][] a){
		boolean[][] b=new boolean[simSize][simSize];
		for(int i=0;i<simSize;i++){
			for(int j=0;j<simSize;j++){
				switch(a[i][j]){
					case NO_DATA:
						//データ未定義
						b[i][j]=false;
						break;
					case BLACK_DATA:
						//黒データ
						b[i][j]=true;
						break;
					case WHITE_DATA:
						//白データ
						b[i][j]=false;
						break;
					case K_DATA:
						//形式、型番情報が入る予定
						b[i][j]=false;
						break;
					case DATA_1:
						//データ1
						b[i][j]=true;
						break;
					case DATA_0:
						//データ0
						b[i][j]=false;
						break;
					case DATA_VER:
						//型番情報
						b[i][j]=true;
						break;
					case DATA_K:
						//形式情報
						b[i][j]=true;
						break;
					default:
						//エラー
						System.out.println("エラー：　シンボルのデータがおかしいです。　："+a[i][j]);
						info.setText(info.getText()+"エラー：　シンボルのデータがおかしいです。　："+a[i][j]+BR);
				}
			}
		}
		return b;
	}

	///////////////////////////////////////////////////////////////////
	////////////////////画面の初期化をするメソッド/////////////////////
	static void init(){
		//変数初期化
			ver=0;
			lv=0;
			encodeMode=0;
			mask=0;
		//入力フィールド初期化
			inputD.setText("");
		//各コンボボックス初期化
			verList.setSelectedIndex(ver);
			lvList.setSelectedIndex(lv);
			maskList.setSelectedIndex(mask);
	}
}
/////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////QRコードの表示クラス////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////

class QRShow extends JFrame{
	ImageIcon icon = new ImageIcon("icon.png");						//アイコンの設定
	public static GPanel[] Grap=new GPanel[QRCode.simbolNumMax];	//グラフィックパネルの型
	public static int blockWidth= 4;									//ブロックの大きさを格納
	public static int quiet=blockWidth*5;							//クワイエットゾーンの大きさを格納
	public int windowSizeX;											//ウインドウの大きさを格納
	public int windowSizeY;											//ウインドウの大きさを格納
	BufferedImage readImage=null;
	int imageX=0;
	int imageY=0;
	QRShow(int simbolNum,int[][] simbol,int simSize,BufferedImage readImage,int imageX,int imageY){
		//グラフィックパネルインスタンス生成
			Grap[simbolNum]=new GPanel(simSize,simbol,readImage,imageX,imageY);
			//サイズ調整
				windowSizeX=simSize*blockWidth+(quiet*2);
				windowSizeY=windowSizeX;
				Grap[simbolNum].setPreferredSize(new Dimension(windowSizeX, windowSizeY));
		//表示
			setSize(windowSizeX+5,windowSizeY+25);
			System.out.println("simSize="+simSize);
			setIconImage(icon.getImage());
			getContentPane().add(Grap[simbolNum],BorderLayout.CENTER);
			setVisible(true);
	}
}
class GPanel extends JPanel{
	int[][] simbol;
	int simSize=0;
	BufferedImage readImage=null;
	int imageX=0;
	int imageY=0;
        
        public static Color NO_DATA_COLOR = Color.BLACK;
        public static Color BLACK_DATA_COLOR = Color.BLACK;
        public static Color DATA_COLOR = Color.BLACK;
        public static Color DATA_VER_COLOR = Color.BLACK;
        public static Color DATA_K_COLOR = Color.BLACK;
        public static Color BACKGROUND_COLOR = Color.WHITE;
        
        public static Color[][] simbolC;
        public static float[][] simbolT;
        
	GPanel(int a,int[][] b,BufferedImage c,int d,int e){
		setBackground(Color.white);
                Image readImageIm;
		simSize=a;
		simbol=new int[simSize][];
		for(int i=0;i<simSize;i++){
			simbol[i]=(int[])b[i].clone();
		}
                try{
                    if(!QREditor.fitMode){
                        readImageIm=c.getScaledInstance((int)((QRCode.initImageSize+QRCode.imageSize)*((float)QRShow.blockWidth)), -1, Image.SCALE_AREA_AVERAGING);
                    }
                    else{
                        readImageIm=c.getScaledInstance((int)(simSize*((float)QRShow.blockWidth)), (int)(simSize*((float)QRShow.blockWidth)), Image.SCALE_AREA_AVERAGING);
                    }
                    readImage=QREditor.toBufferedImage(readImageIm);
                }
                catch(Exception ex){readImage=c;}
		imageX=d;
		imageY=e;
	}
	public void paintComponent(Graphics g) {
	super.paintComponent(g);
            if(!QREditor.swapMode)
            {
		//QRコード描画
                drawDot(g);
                        
		//読み込み画像表示
		drawImage(g);
            }
            else{
                //読み込み画像表示
		drawImage(g);
                //QRコード描画
                drawDot(g);
            }
	}
        private void drawDot(Graphics g)
        {
            Graphics2D g2d = (Graphics2D)g;
            g2d.setColor(Color.BLACK);
            for(int i=0;i<simSize;i++){
                for(int j=0;j<simSize;j++){
                    if(QRCode.inputD.isEditable()==true){
                        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,QREditor.colorTran));
                        if(simbol[i][j]==QRCode.BLACK_DATA || simbol[i][j]==QRCode.DATA_1 || simbol[i][j]==QRCode.DATA_VER || simbol[i][j]==QRCode.DATA_K){
                            g2d.setColor(Color.BLACK);    
                            g2d.fillRect(j*QRShow.blockWidth+QRShow.quiet,i*QRShow.blockWidth+QRShow.quiet,QRShow.blockWidth,QRShow.blockWidth);
                        }
                        else //if(simbol[i][j]==QRCode.DATA_0||simbol[i][j]==QRCode.NO_DATA)
                        {
                            g2d.setColor(BACKGROUND_COLOR);
                            g2d.fillRect(j*QRShow.blockWidth+QRShow.quiet,i*QRShow.blockWidth+QRShow.quiet,QRShow.blockWidth,QRShow.blockWidth);
                        }
                    }
                    else{
                        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,QREditor.colorTran));
                        switch(simbol[i][j]){	
                                case QRCode.BLACK_DATA:
                                    g2d.setColor(BLACK_DATA_COLOR);
                                    break;
                                case QRCode.DATA_1:
                                    if(simbolC[i][j]==NO_DATA_COLOR&&simbolT[i][j]==0.0f){
                                        g2d.setColor(NO_DATA_COLOR);
                                    }
                                    else {
                                        g2d.setColor(simbolC[i][j]);
                                        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,simbolT[i][j]));
                                    }
                                    break;
                                case QRCode.DATA_VER:
                                    g2d.setColor(DATA_VER_COLOR);
                                    break;
                                case QRCode.DATA_K:
                                    g2d.setColor(DATA_K_COLOR);
                                    break;
                                case QRCode.DATA_0:
                                    if(simbolC[i][j]==BACKGROUND_COLOR){
                                        g2d.setColor(BACKGROUND_COLOR);
                                        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,simbolT[i][j]));
                                    }
                                    else{
                                        g2d.setColor(simbolC[i][j]);
                                        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,simbolT[i][j]));
                                    }
                                    break;
                                default:
                                    g2d.setColor(BACKGROUND_COLOR);
                                    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,simbolT[i][j]));
                                    break;
                        }
                        g2d.fillRect(j*QRShow.blockWidth+QRShow.quiet,i*QRShow.blockWidth+QRShow.quiet,QRShow.blockWidth,QRShow.blockWidth);                       
                    }
                }
            }
        }
        private void drawImage(Graphics g)
        {
            if(readImage!=null){
                    Graphics2D g2 = (Graphics2D)g;
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,QREditor.imgTran));
                    g2.drawImage(readImage, (imageX*QRShow.blockWidth)+QRShow.quiet, (imageY*QRShow.blockWidth)+QRShow.quiet, this);
            }
        }
        private void drawFilter(Graphics g)
        {
            
            
        }
}
