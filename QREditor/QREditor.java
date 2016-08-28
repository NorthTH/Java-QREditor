//QR-Code Editor
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.WindowAdapter; 
import java.awt.event.WindowEvent;
import javax.swing.JFileChooser;
import java.awt.image.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.Hashtable;

class QREditor extends JFrame implements ActionListener,KeyListener{

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
		public static final int DATA_E1 = DataBase.DATA_E1;					//定数(エディタでの仮コードデータ1)
		public static final int DATA_E0 = DataBase.DATA_E0;					//定数(エディタでの仮コードデータ0)
		public static final Color TITLE_GRAY = DataBase.TITLE_GRAY;			//定数(タイトル画面背景色)
		public static final Color TITLE_GRAY2 = DataBase.TITLE_GRAY2;		//定数(タイトル画面背景色2)
		public static final String BR = DataBase.BR;						//定数(改行コード)
		public static final String GRAP_SEPARATOR=DataBase.GRAP_SEPARATOR;	//定数(グラフィック部の分離符を格納)
		public static final String BR_IO=DataBase.BR_IO;					//定数(ファイル入出力時の改行コードの置き換え文字)
		int[][][] versionMaxLen=DataBase.getVersionMaxLen();				//QRコード仕様DB
	//GUIオブジェクト宣言
		JComboBox verList;													//型番リスト
		JComboBox lvList;													//誤り訂正リスト
		JComboBox maskList;													//マスクリスト
		//
                JComboBox imgTranList;
                JComboBox regList;
                JComboBox colorTranList;
                //
                JButton openB;														//ファイルオープンボタン
		JButton saveB;														//ファイルセーブボタン
		JButton resetB;														//リセットボタン
		JButton colorB;													//かラー化ボタン
		JButton okB;														//OKボタン
		JButton ok_imgB;													//画像だけ送るボタン
		JButton imageInB;													//画像Inボタン
		JButton addB;														//画像埋め込みボタン
		//
                JButton sizeUp;
                JButton sizeDown;
                JButton fit;
                JButton swap;
                //
                JTextArea dataInputArea;											//データを入力するエリア
		JScrollPane dataInputAreaS;											//↑のスクロール部
		static JRadioButton RadioB_pencil;									//描画モードのラジオボタン(鉛筆)
		static JRadioButton RadioB_eraser;									//描画モードのラジオボタン(消しゴム)
		static JRadioButton RadioB_both;									//描画モードのラジオボタン(鉛筆&消しゴム)
		static JRadioButton RadioB_fill;									//描画モードのラジオボタン(塗りつぶし)
		ButtonGroup RadioBgr = new ButtonGroup();							//描画モードのラジオボタンのグループ
		public static EGPanel EGrap;										//グラフィックオブジェクト
		JScrollPane EGrapS;													//↑のスクロール部
		BufferedImage readImageBuf = null;
		static BufferedImage readImage;
                ColorValueSliderControl tc;
		ImageIcon icon = new ImageIcon("icon.png");							//アイコンの設定
	//ファイルの入出力
		JFileChooser chooser = new JFileChooser();							//ファイルの入出力
	//メニューバー
		MenuItem NewFile	= new MenuItem( "新規作成" );					//[ファイル]->[新規作成]
		MenuItem Open		= new MenuItem( "開く" );						//[ファイル]->[開く]
		MenuItem Save		= new MenuItem( "保存" );						//[ファイル]->[保存]
		MenuItem bOpen		= new MenuItem( "画像ファイル読み込み" );		//[ファイル]->[画像ファイル読み込み]
		MenuItem Exit		= new MenuItem( "エディタを閉じる" );			//[ファイル]->[エディタを閉じる]
		MenuItem Do			= new MenuItem( "データをQR-Code Makerに送る" );//[編集]->[データをQR-Code Makerに送る]
		MenuItem ClearInput	= new MenuItem( "入力エリアのクリア" );			//[表示]->[入力エリアのクリア]
		MenuItem Version	= new MenuItem( "バージョン情報" );				//[ヘルプ]->[バージョン情報]
                
        //その他
		int WindowSizeX=0;													//ウインドウ幅X
		int WindowSizeY=0;													//ウインドウ幅Y
		int encodeMode=0;													//エンコードモード
		int ver=1;															//型番を格納
		int lv=0;															//訂正レベル
		int mask=0;															//マスク
		public static float imgTran = 1.0f;
                public static float colorTran = 1.0f;
                int RSSum=1;														//RSブロック数
		int mode_mozisuLen;													//モード指示子と文字数指示子の長さ
		int verMax=40;														//処理できる最高の型番
		int moveSpeed=EGPanel.blockSize;													//画像ファイル移動スピード	
		String notGrapCode=null;											//グラフィック以外の部分のビット列
		String Inputdata="";												//入力されたデータ
		String dataB;														//データビット列
		String Title="QR-Code Editor";										//プログラムのタイトル
		String outstr=null;													//出力される文字列
		static int paintMode=0;												//描画モード(1:鉛筆 2:消しゴム 3:両方 4:塗りつぶし)
		//
                static boolean fitMode = false;
                static boolean swapMode = false;
                static boolean edit = false;
                //
                static int simSize=21;												//シンボルのサイズ
		static int[][] simbol;												//シンボルを格納
		public static Color[][] simbolC;
                public static float[][] simbolT;
                int [] dataBit;	
                
	////////////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////コンストラクタ////////////////////////////////////////
	QREditor(){
		//初期化
			ver=1;
			lv=0;
			mask=0;
			encodeMode=0;
			simSize=21+4*(ver-1);
			simbol=new int[simSize][simSize];
                        simbolC=new Color[simSize][simSize];
                        simbolT=new float[simSize][simSize];
                //メニュー
			MenuBar MyMenu = new MenuBar();
			Menu FileMenu = new Menu( "ファイル" );
  			  	NewFile.addActionListener(this);
   	 			Open.addActionListener(this);
   	 			Save.addActionListener(this);
   	 			bOpen.addActionListener(this);
   	 			Exit.addActionListener(this);
				FileMenu.add( NewFile );
				FileMenu.add( Open );
				FileMenu.add( Save );
				FileMenu.add( bOpen );
				FileMenu.addSeparator();
				FileMenu.add( Exit );
			Menu EditMenu = new Menu( "編集" );
  			  	Do.addActionListener(this);
				EditMenu.add(Do);
			Menu ShowMenu = new Menu( "表示" );
  			  	ClearInput.addActionListener(this);
				ShowMenu.add(ClearInput);
			Menu HelpMenu = new Menu( "ヘルプ" );
  			  	Version.addActionListener(this);
				HelpMenu.add(Version);
			MyMenu.add( FileMenu );
			MyMenu.add( EditMenu );
			MyMenu.add( ShowMenu );
			MyMenu.add( HelpMenu );
			setMenuBar(MyMenu);
		//ウインドウサイズ調節
			windowResize(simSize);
		//グラフィックエリア
			EGrap=new EGPanel();
			EGrap.addMouseListener(new MyMouseListener());
			EGrap.addMouseMotionListener(new MyMouseMotionListener());
			EGrap.addKeyListener(this);
		//ボタン
			//ファイルオープンボタン
				openB=new JButton("開く");
				openB.addActionListener(this);
				openB.addKeyListener(this);
			//ファイルセーブボタン
				saveB=new JButton("保存");
				saveB.addActionListener(this);
				saveB.addKeyListener(this);
			//リセットボタン
				resetB=new JButton("クリア");
				resetB.addActionListener(this);
				resetB.addKeyListener(this);
			//カラー化ボタン
				colorB=new JButton("カラー化");
				colorB.addActionListener(this);
				colorB.addKeyListener(this);
			//OKボタン
				okB=new JButton("OK");
				okB.addActionListener(this);
				okB.addKeyListener(this);
			//画像だけ送るボタン
				ok_imgB=new JButton("画像だけ送る");
				ok_imgB.addActionListener(this);
				ok_imgB.addKeyListener(this);
			//画像読み込みボタン
				imageInB=new JButton("画");
				imageInB.addActionListener(this);
				imageInB.addKeyListener(this);
			//画像埋め込みボタン
				addB=new JButton("埋");
				addB.addActionListener(this);
				addB.addKeyListener(this);
                        //画像を大きくするボタン
                                sizeUp = new JButton("大");
                                sizeUp.addActionListener(this);
				sizeUp.addKeyListener(this);
                        //画像を小さくするボタン
                                sizeDown = new JButton("小");
                                sizeDown.addActionListener(this);
				sizeDown.addKeyListener(this);
                        // 
                                fit = new JButton("広"); 
                                fit.addActionListener(this);
				fit.addKeyListener(this);
                        //
                                swap = new JButton("裏"); 
                                swap.addActionListener(this);
				swap.addKeyListener(this);
		//データ入力エリア
			dataInputArea=new JTextArea("", 4,38);
			dataInputArea.setLineWrap(true);
			dataInputAreaS=new JScrollPane(dataInputArea);
			dataInputAreaS.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			dataInputAreaS.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			dataInputAreaS.setPreferredSize(new Dimension(WindowSizeX-25, 70));
			dataInputAreaS.addKeyListener(this);
		//グラフィックオブジェクトのスクロール設定
			EGrapS=new JScrollPane(EGrap);
			EGrapS.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			EGrapS.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
			EGrapS.setPreferredSize(new Dimension(500, 420));
			EGrapS.addKeyListener(this);
		//コンボボックス
			//訂正レベル
				lvList = new JComboBox();
				lvList.addItem(" L ( 7%)");
				lvList.addItem(" M (15%)");
				lvList.addItem(" Q (25%)");
				lvList.addItem(" H (30%)");
				lvList.setLightWeightPopupEnabled(false);	//重量コンポーネント化してプルダウンが表示されない不具合解消
				lvList.addActionListener(this);
				lvList.addKeyListener(this);
			//型番
				verList = new JComboBox();
				for(int i=1;i<=verMax;i++){
					verList.addItem(Integer.toString(i)+" ");
				}
				verList.setLightWeightPopupEnabled(false);	//重量コンポーネント化してプルダウンが表示されない不具合解消
				verList.addActionListener(this);
				verList.addKeyListener(this);
			//マスク
				maskList = new JComboBox();
				for(int i=0;i<8;i++){
					maskList.addItem(Integer.toString(i)+"  ");
				}
				maskList.setLightWeightPopupEnabled(false);	//重量コンポーネント化してプルダウンが表示されない不具合解消
				maskList.addActionListener(this);
				maskList.addKeyListener(this);
                        //イメージ透明
                                imgTranList = new JComboBox();
                                for(int i=100;i>=0;i--){
					imgTranList.addItem(Integer.toString(i)+" %");
				}
                                imgTranList.setSelectedIndex(100);
                                imgTranList.setLightWeightPopupEnabled(false);	//重量コンポーネント化してプルダウンが表示されない不具合解消
				imgTranList.addActionListener(this);
				imgTranList.addKeyListener(this);
                        //明暗認識
                                regList = new JComboBox();
                                for(int i=0;i<=100;i++){
					regList.addItem(Integer.toString(i)+" %");
				}
                                regList.setSelectedIndex(80);
                                regList.setLightWeightPopupEnabled(false);	//重量コンポーネント化してプルダウンが表示されない不具合解消
				regList.addActionListener(this);
				regList.addKeyListener(this);
                        //ドット透明
                                colorTranList = new JComboBox();
                                for(int i=100;i>=0;i--){
					colorTranList.addItem(Integer.toString(i)+" %");
				}
                                colorTranList.setSelectedIndex(100);
                                colorTranList.setLightWeightPopupEnabled(false);	//重量コンポーネント化してプルダウンが表示されない不具合解消
				colorTranList.addActionListener(this);
				colorTranList.addKeyListener(this);
		//ラジオボタン
			paintMode=0;
			RadioB_pencil=new JRadioButton("鉛筆",true);
			RadioB_eraser=new JRadioButton("消しゴム");
			RadioB_both=new JRadioButton("領域&背景");
			RadioB_fill=new JRadioButton("塗りつぶし");
			RadioBgr=new ButtonGroup();
			RadioBgr.add(RadioB_pencil);
			RadioBgr.add(RadioB_eraser);
			RadioBgr.add(RadioB_both);
			RadioBgr.add(RadioB_fill);
			RadioB_pencil.addActionListener(this);
			RadioB_pencil.addKeyListener(this);
			RadioB_eraser.addActionListener(this);
			RadioB_eraser.addKeyListener(this);
			RadioB_both.addActionListener(this);
			RadioB_both.addKeyListener(this);
			RadioB_fill.addActionListener(this);
			RadioB_fill.addKeyListener(this);
		//レイアウト
			setFocusable(true);
			addKeyListener(this);
			getContentPane().setBackground(TITLE_GRAY);
			Panel p1,p2,p3,p4,p5,p6,p7;
			getContentPane().setLayout(new BoxLayout(getContentPane(),BoxLayout.Y_AXIS));
			getContentPane().add(p1=new Panel());
			getContentPane().add(p2=new Panel());
			getContentPane().add(p3=new Panel());
			getContentPane().add(p4=new Panel());
			getContentPane().add(p5=new Panel());
                        getContentPane().add(p7=new Panel());
			getContentPane().add(p6=new Panel());
			p1.setLayout(new FlowLayout(FlowLayout.LEFT));
			p1.addKeyListener(this);
			p1.add(new JLabel("QRコード化するデータ"));
			p2.setLayout(new FlowLayout());
			p2.addKeyListener(this);
			p2.add(dataInputAreaS);
			p3.addKeyListener(this);
			p3.add(new JLabel("訂正"));
			p3.add(lvList);
			p3.add(new JLabel("型番"));
			p3.add(verList);
			p3.add(new JLabel("マスク"));
			p3.add(maskList);
			p3.add(imageInB);
			p3.add(addB);
			p3.add(colorB);
			p4.addKeyListener(this);
			p4.add(new JLabel("描画モード："));
			p4.add(RadioB_pencil);
			p4.add(RadioB_eraser);
			p4.add(RadioB_both);
			p4.add(RadioB_fill);
                        p4.add(new JLabel("輝度値"));
                        p4.add(regList);
			EGrap.setPreferredSize(new Dimension(simSize*EGPanel.blockSize, simSize*EGPanel.blockSize));
			p5.addKeyListener(this);
			p5.add(EGrapS);
			p6.addKeyListener(this);
			p6.setLayout(new FlowLayout());
			p6.add(openB);
			p6.add(saveB);
			p6.add(resetB);
			p6.add(ok_imgB);
			p6.add(okB);
                        //
                        p7.addKeyListener(this);
                        p7.add(new JLabel("画像編集"));
                        p7.add(sizeUp);
                        p7.add(sizeDown);
                        p7.add(fit);
                        p7.add(swap);
                        p7.add(new JLabel("透明"));
                        p7.add(imgTranList);
                        p7.add(colorTranList);
                        //
			setIconImage(icon.getImage());
		//表示
			init();
			setTitle(Title);
			setVisible(true);
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////イベントメソッド////////////////////////////////////////
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		//ボタン(開く)のイベント
			if(obj==openB){
				int returnVal = chooser.showOpenDialog(this);
				try {
					if (returnVal == JFileChooser.APPROVE_OPTION) {
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
									if(ioData2.length==simSizeTmp){
										for(int j=0;j<simSizeTmp;j++){
											simbolTmp[i][j]=Integer.parseInt(ioData2[j]);
										}
									}else{
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
								setTitle(Title+" "+file.getAbsolutePath());
								ver=Integer.parseInt(ioData[0]);
								encodeMode=Integer.parseInt(ioData[1]);
								lv=Integer.parseInt(ioData[2]);
								mask=Integer.parseInt(ioData[3]);
								verList.setSelectedIndex(ver-1);
								lvList.setSelectedIndex(lv);
								maskList.setSelectedIndex(mask);
								simSize = 21+(4*(ver-1));
								simbol=new int[simSize][];
								for(int i=0;i<simSize;i++){
									simbol[i]=(int[])simbolTmp[i].clone();
								}
								dataInputArea.setText(str1);
								str2=str2.replaceAll(BR_IO,BR);
								outstr=str2;
								update();
							}
						in.close();
					}
				} catch(Exception ex){}
			}
		//ボタン(保存)のイベント
			if(obj==saveB){
				data_out();
				int returnVal = chooser.showSaveDialog(this);
				try{
					if (returnVal == JFileChooser.APPROVE_OPTION) {
						BufferedWriter out = new BufferedWriter(new FileWriter(chooser.getSelectedFile()));
						//型番、モード、訂正レベル、マスク書き出し
							String[] ioData=new String[1];
							ioData[0]=ver+","+encodeMode+","+lv+","+mask;
							out.write(ioData[0]);
							out.newLine();
						//シンボル書き出し
							for(int i=0;i<simSize;i++){
								ioData[0]="";
								for(int j=0;j<simSize;j++){
									ioData[0]+=simbol[i][j]+",";
								}
								out.write(ioData[0]);
								out.newLine();
							}
						//入力データ書き出し
							out.write(Inputdata);
							out.newLine();
						//出力データ書き出し
							String str=outstr;
							str=str.replaceAll(BR,BR_IO);
							out.write(str);
						out.close();
					}
				}
				catch(Exception ex){
					ex.printStackTrace();
				}
			}
		//ボタン(カラー化)のイベント
			if(obj==colorB){
				tc = new ColorValueSliderControl();
                                tc.setLocation(0, 0);
			}
		//ボタン(リセット)のイベント
			if(obj==resetB){
				init();
			
			}
		//ボタン(OK)のイベント
			if(obj==okB){
				data_out();
				QRCode.lv=lv;
				QRCode.encodeMode=encodeMode;
				QRCode.ver=ver;
				QRCode.inputD.setText(outstr);
				QRCode.verList.setSelectedIndex(ver);
				QRCode.lvList.setSelectedIndex(lv);
				QRCode.maskList.setSelectedIndex(mask+1);
                                
                                GPanel.NO_DATA_COLOR = EGPanel.NO_DATA_COLOR;
                                GPanel.BLACK_DATA_COLOR = EGPanel.BLACK_DATA_COLOR;
                                GPanel.DATA_COLOR = EGPanel.DATA_COLOR;
                                GPanel.DATA_VER_COLOR = EGPanel.DATA_VER_COLOR;
                                GPanel.DATA_K_COLOR = EGPanel.DATA_K_COLOR;
                                GPanel.BACKGROUND_COLOR = EGPanel.BACKGROUND_COLOR;
                                
                                GPanel.simbolC = QREditor.simbolC;
                                GPanel.simbolT = QREditor.simbolT;
                                
                                QRCode.inputD.setEditable(false);
                                
				if(readImage!=null && EGPanel.imgVisible){
					QRCode.readImage=readImageBuf;
					//QRCode.imageX=(int)(EGPanel.imageX/(float)(EGPanel.blockSize/QRShow.blockWidth));
					//QRCode.imageY=(int)(EGPanel.imageY/(float)(EGPanel.blockSize/QRShow.blockWidth));  
                                        QRCode.imageX=(int)(EGPanel.imageX/(float)(EGPanel.blockSize));
					QRCode.imageY=(int)(EGPanel.imageY/(float)(EGPanel.blockSize));
				}
			}
		//ボタン(画像だけ送る)のイベント
			if(obj==ok_imgB){
				QRCode.ver=ver;
				QRCode.verList.setSelectedIndex(ver);
				if(readImage!=null && EGPanel.imgVisible){
					QRCode.readImage=readImageBuf;
					//QRCode.imageX=(int)(EGPanel.imageX/(float)(EGPanel.blockSize/QRShow.blockWidth));
					//QRCode.imageY=(int)(EGPanel.imageY/(float)(EGPanel.blockSize/QRShow.blockWidth));
                                        QRCode.imageX=(int)(EGPanel.imageX/(float)(EGPanel.blockSize));
					QRCode.imageY=(int)(EGPanel.imageY/(float)(EGPanel.blockSize));
				}
			}
		//ボタン(画像In)のイベント
			if(obj==imageInB){
				Image readImageIm;
				int returnVal = chooser.showOpenDialog(this);
				try {
					if (returnVal == JFileChooser.APPROVE_OPTION) {
						File file = chooser.getSelectedFile();
						try {
 							readImageBuf = ImageIO.read(file);
                                                        QRCode.imageSize = 0;
							//画像サイズの調整
								//readImageIm=readImageBuf.getScaledInstance((int)(readImageBuf.getWidth()*((float)EGPanel.blockSize/QRShow.blockWidth)), -1, Image.SCALE_AREA_AVERAGING);
                                                                readImageIm=readImageBuf.getScaledInstance((int)(QRCode.initImageSize*((float)EGPanel.blockSize))-1, -1, Image.SCALE_AREA_AVERAGING);
							//ImageをBufferedImageに変換
								readImage=toBufferedImage(readImageIm);
						} catch (Exception e2) {
							e2.printStackTrace();
							readImageBuf = null;
						}
					}
				} catch(Exception ex){}



				update();
			}
		//ボタン(画像埋め込み)のイベント
			if(obj==addB && readImage!=null){
				int sp_x=0;
				int sp_y=0;
				int ep_x=0;
				int ep_y=0;
				int x=0;
				int y=0;
				int rgb=0;
				//減色処理
					readImage=imgToGray(readImage);
				//メイン処理
					sp_x=EGPanel.imageX/EGPanel.blockSize;
					sp_y=EGPanel.imageY/EGPanel.blockSize;
					ep_x=(EGPanel.imageX+readImage.getWidth())/EGPanel.blockSize;
					ep_y=(EGPanel.imageY+readImage.getHeight())/EGPanel.blockSize;
					for(int i=sp_x;i<=ep_x;i++){
						for(int j=sp_y;j<=ep_y;j++){
							//System.out.println("x="+(i*EGPanel.blockSize+(EGPanel.blockSize/2)-(sp_x*EGPanel.blockSize)));
							x=i*EGPanel.blockSize+(EGPanel.blockSize/2)-(sp_x*EGPanel.blockSize);
							y=j*EGPanel.blockSize+(EGPanel.blockSize/2)-(sp_y*EGPanel.blockSize);
							if(x>=0 && x<=readImage.getWidth() && y>=0 && y<=readImage.getHeight()){
								System.out.println("getRGB="+readImage.getRGB(x,y));
								rgb=readImage.getRGB(x,y);
							}else{
								//デバッグ用
								System.out.println("座標が画像のピクセル外 x="+x+" y="+y);
							}
							if(rgb!=-1){
								EGPanel.bitTurn(i,j);
							}
						}
					}
				update();
			}
		//コンボボックス(型番)のイベント
			if(obj==verList){
				ver=verList.getSelectedIndex()+1;
				init();
			}
		//コンボボックス(訂正レベル)のイベント
			if(obj==lvList){
				lv=lvList.getSelectedIndex();
				init();
			}
		//コンボボックス(マスク)のイベント
			if(obj==maskList){
				mask=maskList.getSelectedIndex();
				init();
			}
                //イメージ透明
                        if(obj==imgTranList){
				imgTran = (float)imgTranList.getSelectedIndex()/100;
				update();
			}
                //明暗認識
                        if(obj==regList){
				EGPanel.regList = regList.getSelectedIndex();
                                //JOptionPane.showMessageDialog(null,regList.getSelectedIndex());
                                JOptionPane.showMessageDialog(null, "明暗輝度値は"+regList.getSelectedIndex()
                                            + "に設定されました。\n"
                                            +"輝度値を設定することは一回のみ\n"
                                            + "変更したい場合は『クリア』ボタンで全てをリセットしてくさだい。");
                                regList.setEnabled(false);
                                edit = true;
				update();
			}
                //ドット透明
                        if(obj==colorTranList){
				colorTran = (float)colorTranList.getSelectedIndex()/100;
				update();
			}
		//ラジオボタン(鉛筆)のイベント
			if(obj==RadioB_pencil){
				paintMode=0;
			}
		//ラジオボタン(消しゴム)のイベント
			if(obj==RadioB_eraser){
				paintMode=1;
			}
		//ラジオボタン(鉛筆&消しゴム)のイベント
			if(obj==RadioB_both){
				paintMode=2;
			}
		//ラジオボタン(塗りつぶし)のイベント
			if(obj==RadioB_fill){
				paintMode=3;
			}
                        
                        if(obj==sizeUp){
                            if(EGPanel.imgVisible && readImage!=null){    
                                QRCode.imageSize += 1;
                            }
                            update();
			}
                        
                        if(obj==sizeDown){
                            if(EGPanel.imgVisible && readImage!=null){   
				QRCode.imageSize -= 1;
                            }
                            update();
			}
                        if(obj==fit){
                            if(EGPanel.imgVisible && readImage!=null){ 
                                if(fitMode){
                                    fitMode = false;
                                }
                                else{
                                    fitMode = true;
                                    EGPanel.imageX = 0;
                                    EGPanel.imageY = 0;
                                }
                            }
                            update();
			}
                        if(obj==swap){
                            if(EGPanel.imgVisible && readImage!=null){ 
                                if(swapMode){
                                    swapMode = false;
                                }
                                else{
                                    swapMode = true;
                                }
                            }
                            update();
			}
	}

	/////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////メソッド//////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////


	//////////////////////////////////////////////////////////////
	//////////////////シンボルを初期化するメソッド////////////////
	public void init (){
		simSize = 21+(4*(ver-1));
		simbol = new int[simSize][simSize];
		simbolC = new Color[simSize][simSize];
                simbolT = new float[simSize][simSize];
                setSimbolInitData();
		readImage=null;
		EGPanel.imageX=0;
		EGPanel.imageY=0;
		//画面更新
                regList.setEnabled(true);
                edit = false;
		update();
	}

	//////////////////////////////////////////////////////////////
	///////////////////画面を更新するメソッド/////////////////////
	public void update (){
		windowResize(simSize);
                try{
                    Image readImageIm;
                    if(!fitMode)
                    {
                        readImageIm=readImageBuf.getScaledInstance((int)((QRCode.initImageSize+QRCode.imageSize)*((float)EGPanel.blockSize))-1, -1, Image.SCALE_AREA_AVERAGING);
                    }
                    else
                    {
                        readImageIm=readImageBuf.getScaledInstance((int)(simSize*((float)EGPanel.blockSize)), (int)(simSize*((float)EGPanel.blockSize)), Image.SCALE_AREA_AVERAGING);
                    }
                    readImage=toBufferedImage(readImageIm);
                }
                catch(Exception ex){}
		EGrap.repaint();
		EGrap.setPreferredSize(new Dimension(simSize*EGPanel.blockSize, simSize*EGPanel.blockSize));
		EGrap.revalidate();
	}

	//////////////////////////////////////////////////////////////
	////////////クリックドラッグ時に実行されるメソッド////////////
	public static void Clicked(int x,int y) {
		EGrap.Clicked(x,y,paintMode);
	}

	//////////////////////////////////////////////////////////////
	/////////ドラッグが終わったら座標をリセットするメソッド///////
	public static void Released() {
		EGrap.Released();
	}

	//////////////////////////////////////////////////////////////
	////////////ウインドウサイズを適正にするメソッド//////////////
	public void windowResize(int simSize){
		WindowSizeX=650;
		WindowSizeY=670;
		setSize(WindowSizeX,WindowSizeY);
	}

	//////////////////////////////////////////////////////////////////////
	/////////////////////////////出力メソッド/////////////////////////////
	public void data_out(){
		RSSum=versionMaxLen[ver][lv][6]+versionMaxLen[ver][lv][7];
		dataB="";
		String[] dataB8; 
		//シンボルのコピーsimbol_copyを取得
			int[][] simbol_copy=new int[simSize][];
			for(int i=0;i<simSize;i++){
				simbol_copy[i]=(int[])simbol[i].clone();
			}
		//データ部分にマスクをかける
			DataBase.mask(simbol_copy,mask,simSize,2);
		//グラフィック部分のビット列を取得
			//シンボルの全ビット列抽出
					dataB=getBit(simSize,simbol_copy,0);
			//RSBが複数の時はインタリーブ取得
				if(RSSum>1){
					String[][] RSB=new String[RSSum][];
					int dBkazu=(int)(versionMaxLen[ver][lv][4]/RSSum);
					for(int i=0;i<versionMaxLen[ver][lv][6];i++){
						RSB[i]=new String[dBkazu];
					}
					for(int i=versionMaxLen[ver][lv][6];i<RSSum;i++){
						RSB[i]=new String[dBkazu+1];
					}
					//データビット列を8ビットずつに区切る
						dataB8=new String[dataB.length()/8];
						for(int i=0;i<dataB8.length;i++){
							dataB8[i]=dataB.substring((i*8),(i*8)+8);
						}
					//RSB配列に順番にデータビット列を格納していく
						int nowRSB=0;
						for(int i=0;i<dataB8.length;i++){
							if(i==RSSum*dBkazu){
								nowRSB=versionMaxLen[ver][lv][6];
							}
							RSB[nowRSB][(int)i/RSSum]=dataB8[i];
							nowRSB++;
							nowRSB%=RSSum;
						}
					//RSB配列を１つにまとめる
						dataB="";
						for(int i=0;i<RSSum;i++){
							for(int j=0;j<RSB[i].length;j++){
								dataB+=RSB[i][j];
							}
						}
				}
		//全ビット列からグラフィック部分のビット列のみを取得
			dataB=dataB.substring(mode_mozisuLen);
		//データの出力
			outstr=Inputdata+GRAP_SEPARATOR+dataB;
	}

	//////////////////////////////////////////////////////////////////////
	///////////////シンボルに初期データビットを設定するメソッド///////////
	public void setSimbolInitData(){
		//入力データ取得
			Inputdata=dataInputArea.getText();
			//入力データが容量を超えていたら入力データ調整
				//作成中
			encodeMode=DataBase.getEncodeMode(Inputdata);
			String InputdataBit=DataBase.DtoB(Inputdata,encodeMode);
		//グラフィックビット前の全ビット数を計算
			mode_mozisuLen=4+DataBase.mozisusizisiLenGet(ver,encodeMode);
			mode_mozisuLen+=InputdataBit.length();
			//終端パターンの数を計算
				String terminator="";
				int terminatorLen=((versionMaxLen[ver][lv][4]*8)-mode_mozisuLen);
				terminatorLen=terminatorLen>4?4:terminatorLen;
				//デバッグ用
					System.out.println("終端パターン数="+terminatorLen);
				for(int i=0;i<terminatorLen;i++){
					terminator+="0";
					mode_mozisuLen++;
				}
		//グラフィックビット前の全ビットデータを取得
			notGrapCode=DataBase.getMode(encodeMode);														//データモード指示子
			notGrapCode+=DataBase.get2(Inputdata.length(),DataBase.mozisusizisiLenGet(ver,encodeMode));		//データ文字数指示子
			notGrapCode+=InputdataBit;																		//データ
			notGrapCode+=terminator;																		//終端パターン
			//グラフィック部分前の全ビットデータを、シンボルに格納する形に変換
				dataBit=new int[versionMaxLen[ver][lv][4]*8];
				for(int i=0;i<notGrapCode.length();i++){
					if(notGrapCode.charAt(i)=='0'){
						dataBit[i]=DATA_E0;
					}else{
						dataBit[i]=DATA_E1;
					}
				}
				for(int i=mode_mozisuLen;i<versionMaxLen[ver][lv][4]*8;i++){
					dataBit[i]=DATA_0;
				}
		//インタリーブ配置
			RSSum=versionMaxLen[ver][lv][6]+versionMaxLen[ver][lv][7];
			if(RSSum>1){	
				String[] dataB8;
				String[][] RSB=new String[RSSum][];
				int dBkazu=(int)(versionMaxLen[ver][lv][4]/RSSum);
				dataB8=new String[dataBit.length/8];
				for(int i=0;i<dataB8.length;i++){
					dataB8[i]="";
					for(int j=0;j<8;j++){
						dataB8[i]+=Integer.toString(dataBit[(i*8)+j]);
					}
				}
				for(int i=0;i<versionMaxLen[ver][lv][6];i++){
					RSB[i]=new String[dBkazu];
					}
				for(int i=versionMaxLen[ver][lv][6];i<RSSum;i++){
					RSB[i]=new String[dBkazu+1];
				}
				//１つめのRSブロック数
					int tmp=0;
					for(int i=0;i<versionMaxLen[ver][lv][6];i++){
						System.out.println("1つめのRSブロック");
						dBkazu=(int)(versionMaxLen[ver][lv][4]/RSSum);
						RSB[i]=new String[dBkazu];
						for(int j=0;j<dBkazu;j++){
							RSB[i][j]=dataB8[tmp++];
						}
					}
				//2つめのRSブロック数
					for(int i=0;i<versionMaxLen[ver][lv][7];i++){
						System.out.println("2つめのRSブロック");
						dBkazu=(int)(versionMaxLen[ver][lv][4]/RSSum)+1;
						RSB[i+versionMaxLen[ver][lv][6]]=new String[dBkazu];
						for(int j=0;j<dBkazu;j++){
							RSB[i+versionMaxLen[ver][lv][6]][j]=dataB8[tmp++];
						}
					}
				//データを配置する順にソート
					dataBit=new int[versionMaxLen[ver][lv][4]*8];
					tmp=0;
					for(int i=0;i<Math.floor(versionMaxLen[ver][lv][4]/RSSum);i++){
						for(int j=0;j<RSB.length;j++){
							for(int k=0;k<8;k++){
								dataBit[tmp++]=Integer.parseInt(String.valueOf(RSB[j][i].charAt(k)));
							}
						}
					}
					for(int i=versionMaxLen[ver][lv][6];i<RSSum;i++){
						for(int k=0;k<8;k++){
							dataBit[tmp++]=Integer.parseInt(String.valueOf(RSB[i][((int)(versionMaxLen[ver][lv][4]/RSSum))].charAt(k)));
						}
					}
			}
		//シンボルに固定データ埋め込み
			DataBase.Indata(ver,simSize,simbol);
		//ビット列をシンボルに埋め込む
			setBit(simSize,dataBit,simbol);
		//E1,E0のみマスクを適用
			DataBase.mask(simbol,mask,simSize,1);
		//形式情報を埋め込む
			DataBase.setFormatInformation(simbol,lv,mask);
	}

	//////////////////////////////////////////////////////////////
	////////シンボルにデータビット列を挿入するメソッド////////////
	public void setBit(int simSize,int[] haitiBit,int[][] simbol){
		//初期設定
			int direct=1;
		  	int LeftOrRight=1;
		  	int nowX=simSize-1;
		  	int nowY=simSize-1;
			int haitiBitLength;
			int[] tmp=new int[2];
		//配置
			haitiBitLength=haitiBit.length;
			tmp[1]=0;
			for(int i=0;tmp[1]<haitiBit.length;i++){
				tmp[0]=0;
				//デバッグ用
					if(nowX<-1){
						System.out.println("エラー：　データがシンボルに収まりきりません　　残りビット数："+(haitiBit.length-i-1));
						QRCode.info.setText(QRCode.info.getText()+"エラー：　データがシンボルに収まりきりません　　残りビット数："+(haitiBit.length-i-1)+BR);
					}
				if(nowX>=0 && nowX<=simSize-1 && nowY>=0 && nowY<=simSize-1){
					//データをそれぞれ配置
						if(simbol[nowY][nowX]==0){
							if(haitiBit[tmp[1]]==DATA_E1){
								simbol[nowY][nowX]=DATA_E1;
							}else if(haitiBit[tmp[1]]==DATA_E0){
								simbol[nowY][nowX]=DATA_E0;
							}else if(haitiBit[tmp[1]]==DATA_0){
									simbol[nowY][nowX]=DATA_0;
                                                                        simbolC[nowY][nowX]=Color.WHITE;
                                                                        simbolT[nowY][nowX]=0.0f;
							}
							tmp[0]=1;
							tmp[1]++;
						}
				}

				if(tmp[0]==0){
					//移動
						if(LeftOrRight==1){
							//右側
							nowX--;
							LeftOrRight=-LeftOrRight;
						}else if(nowY>=0 && nowY<=simSize-1){
							//上下移動可能
							nowY-=direct;
							nowX++;
							LeftOrRight=-LeftOrRight;
						}else{
							//上下移動不可能
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
	}

	//////////////////////////////////////////////////////////////
	////////シンボルからデータビット列を取り出すメソッド//////////
	public String getBit(int simSize,int[][] simbol,int mode){
		//
		//mode=1：グラフィック部分のビット抽出
		//mode=2：グラフィック以外の部分のビット抽出
		//mode=0：両方抽出
		//
		//初期設定
			int direct=1;
		  	int LeftOrRight=1;
		  	int nowX=simSize-1;
		  	int nowY=simSize-1;
			int[] tmp=new int[2];
			String dataB="";
		//配置
			tmp[1]=0;
			for(int i=0;tmp[1]<versionMaxLen[ver][lv][4]*8;i++){
				tmp[0]=0;
				//デバッグ用
					if(nowX<-1){
						//エラー
					}
				if(nowX>=0 && nowX<=simSize-1 && nowY>=0 && nowY<=simSize-1){
					//データを取り出し
							if(simbol[nowY][nowX]==DATA_E1){
								if(mode==2 || mode==0){
									dataB+="1";
								}
								tmp[1]++;
							}else if(simbol[nowY][nowX]==DATA_E0){
								if(mode==2 || mode==0){
									dataB+="0";
								}
								tmp[1]++;
							}
							if(simbol[nowY][nowX]==DATA_1){
								if(mode==1 || mode==0){
									dataB+="1";
								}
								tmp[1]++;
							}else if(simbol[nowY][nowX]==DATA_0){
								if(mode==1 || mode==0){
									dataB+="0";
								}
								tmp[1]++;
							}
				}
				if(tmp[0]==0){
					//移動
						if(LeftOrRight==1){
							//右側
							nowX--;
							LeftOrRight=-LeftOrRight;
						}else if(nowY>=0 && nowY<=simSize-1){
							//上下移動可能
							nowY-=direct;
							nowX++;
							LeftOrRight=-LeftOrRight;
						}else{
							//上下移動不可能
							nowX--;
							if(nowX==6){
								nowX--;
							}
							LeftOrRight=-LeftOrRight;
							nowY+=direct;
							direct=-direct;	
						}
						if(nowX<-1){
							System.out.println("エラー：getBit()にてシンボルを取得しきれませんでした。");
							QRCode.info.setText(QRCode.info.getText()+"エラー：getBit()にてシンボルを取得しきれませんでした。"+BR);
							break; 
						}
			 	}
			}
		return dataB;
	}

	//////////////////////////////////////////////////////////////
	////////////ImageをBufferedImageに変換するメソッド////////////
	static public BufferedImage toBufferedImage(Image img){
		BufferedImage bimg=null;
		try{
			//java.awt.MediaTracker でロードを待機
				MediaTracker tracker = new MediaTracker(new Component(){});
				tracker.addImage(img, 0);
				tracker.waitForAll();
      		//ImageをBufferedImageに変換
				PixelGrabber pixelGrabber = new PixelGrabber(img, 0, 0, -1, -1, false);
				pixelGrabber.grabPixels();
				ColorModel cm = pixelGrabber.getColorModel();
				final int w = pixelGrabber.getWidth();
				final int h = pixelGrabber.getHeight();
				WritableRaster raster = cm.createCompatibleWritableRaster(w, h);
				bimg=new BufferedImage(cm,raster,cm.isAlphaPremultiplied(),new Hashtable());
				bimg.getRaster().setDataElements(0, 0, w, h, pixelGrabber.getPixels());
		}catch(InterruptedException e){}
		return bimg;
	}

	//////////////////////////////////////////////////////////////
	/////////画像をグレースケールに変換するメソッド///////////////
	public BufferedImage imgToGray(BufferedImage bimg){
		for (int iy = 0; iy < bimg.getHeight(); iy++) {
			for (int ix = 0; ix < bimg.getWidth(); ix++) {
				int col = bimg.getRGB(ix, iy);
				bimg.setRGB(ix, iy, toGray(col));
			}
		}
		return bimg;
	}

	//////////////////////////////////////////////////////////////
	/////////画素をグレースケールに変換するメソッド///////////////
	public int toGray(int col){
		Color c = new Color(col);
		int max = Math.max(c.getRed(), Math.max(c.getGreen(), c.getBlue()));
		int min = Math.min(c.getRed(), Math.min(c.getGreen(), c.getBlue()));
		int a = c.getAlpha();
		int v = (max+min)/2;
		c = new Color(v, v, v, a);
		return c.getRGB();
	}

	//////////////////////////////////////////////////////////////
	//////////////キーボード入力イベントメソッド//////////////////
	public void keyPressed(KeyEvent e) {
            //画像の移動操作
		System.out.println("押されたキーコード:"+e.getKeyCode());
		if(e.getKeyCode()==37){//←
			EGPanel.imageX-=moveSpeed;
		}else if(e.getKeyCode()==39){//→
			EGPanel.imageX+=moveSpeed;
		}else if(e.getKeyCode()==38){//↑
			EGPanel.imageY-=moveSpeed;
		}else if(e.getKeyCode()==40){//↓
			EGPanel.imageY+=moveSpeed;
		}else if(e.getKeyCode()==67){//C　画像の表示切替
			if(EGPanel.imgVisible){
				EGPanel.imgVisible=false;
			}else{
				EGPanel.imgVisible=true;
			}
		}
		if(moveSpeed<2*EGPanel.blockSize){
			moveSpeed+=EGPanel.blockSize;
		}
		update();
	}
	public void keyReleased(KeyEvent e) {
		moveSpeed=EGPanel.blockSize;
	}
	public void keyTyped(KeyEvent e) {
	}
}

class MyMouseListener extends MouseAdapter{
	public void mousePressed(MouseEvent e) {
            if(QREditor.edit)
		QREditor.Clicked(e.getX(),e.getY());
            else
                JOptionPane.showMessageDialog(null,"輝度値を設定してください");
	}
}

class MyMouseMotionListener implements MouseMotionListener{
	public void mouseDragged( MouseEvent e ) {
            if(QREditor.edit)
		QREditor.Clicked(e.getX(),e.getY());
            else
                JOptionPane.showMessageDialog(null,"輝度値を設定してください");
	}
	public void mouseMoved( MouseEvent e ) {
            if(QREditor.edit)
                QREditor.Released();
	}

}

/////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////グラフィックパネル////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////
class EGPanel extends JPanel{
	//DataBaseからデータ取得
		public static final int NO_DATA = DataBase.NO_DATA;					//定数(データ無)
		public static final int BLACK_DATA = DataBase.BLACK_DATA;			//定数(黒データ)
		public static final int WHITE_DATA = DataBase.WHITE_DATA;			//定数(白データ)
		public static final int K_DATA = DataBase.K_DATA;					//定数(形式、型番予定)
		public static final int DATA_1 = DataBase.DATA_1;					//定数(コードデータ1)
		public static final int DATA_0 = DataBase.DATA_0;					//定数(コードデータ0)
		public static final int DATA_VER = DataBase.DATA_VER;				//定数(型番情報)
		public static final int DATA_K = DataBase.DATA_K;					//定数(形式情報)
		public static final int DATA_E1 = DataBase.DATA_E1;					//定数(エディタでの仮コードデータ1)
		public static final int DATA_E0 = DataBase.DATA_E0;					//定数(エディタでの仮コードデータ0)
		public static final String BR = DataBase.BR;						//定数(改行コード)
	//ドットのカラー
                public static Color rgbValue = Color.BLACK;
                public static double briV = 0;
                public static Color NO_DATA_COLOR = Color.BLACK;
                public static Color BLACK_DATA_COLOR = Color.BLACK;
                public static Color K_DATA_COLOR = Color.GRAY;
                public static Color DATA_COLOR = Color.BLACK;
                public static Color DATA_VER_COLOR = Color.BLACK;
                public static Color DATA_K_COLOR = Color.BLACK;
                public static Color DATA_E_COLOR = Color.BLACK;
                public static Color BACKGROUND_COLOR = Color.WHITE;
        //その他
		static int quiet=1;
		static int blockWidth=9;
		static int blockSize=blockWidth+quiet;
		static int imageX=0;
		static int imageY=0;
		static boolean imgVisible=true;										//画像ファイルの表示切替
		int nowX=-1;
		int nowY=-1;
                static int regList = 80;
                //static int regWhite = 80;										//データビット列を取得
                public static float tranValue = 1.0f;
	EGPanel(){

	}
        
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            if(QREditor.swapMode){
                drawImage(g);
                drawDot(g);
            }
            else{
                drawDot(g);
                    //読み込み画像表示
                drawImage(g);
            }
	}
        private void drawDot(Graphics g)
        {
            //QRコード外枠描画
            Graphics2D g2d = (Graphics2D)g;
            g2d.setColor(Color.black);
            //g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.5f));
            for(int i=0;i<QREditor.simSize;i++){
                for(int j=0;j<QREditor.simSize;j++){
                    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.5f));
                    switch(QREditor.simbol[j][i]){
                        case NO_DATA:
                                //データ未定義
                                QREditor.simbolC[j][i] = NO_DATA_COLOR;
                                //QREditor.simbolT[j][i] = tranValue;
                                g2d.setColor(NO_DATA_COLOR);
                                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.5f));
                                g2d.drawRect(i*blockSize,j*blockSize,blockWidth,blockWidth);
                                g2d.setColor(Color.LIGHT_GRAY);
                                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,QREditor.colorTran));
                                g2d.fillRect(i*blockSize,j*blockSize,blockWidth,blockWidth);
                                break;
                        case BLACK_DATA:
                                //黒データ
                                g2d.setColor(Color.BLUE);
                                g2d.drawRect(i*blockSize,j*blockSize,blockWidth,blockWidth);
                                g2d.setColor(BLACK_DATA_COLOR);
                                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,QREditor.colorTran));
                                g2d.fillRect(i*blockSize,j*blockSize,blockWidth,blockWidth);
                                break;
                        case WHITE_DATA:
                                //白データ
                                g2d.setColor(Color.BLUE);
                                g2d.drawRect(i*blockSize,j*blockSize,blockWidth,blockWidth);
                                g2d.setColor(BACKGROUND_COLOR);
                                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.3f));
                                g2d.fillRect(i*blockSize,j*blockSize,blockWidth,blockWidth);
                                break;
                        case K_DATA:
                                //形式、型番情報が入る予定地
                                g2d.setColor(K_DATA_COLOR);
                                g2d.drawRect(i*blockSize,j*blockSize,blockWidth,blockWidth);
                                break;
                        case DATA_1:
                                //データ1
                                g2d.setColor(Color.BLACK);
                                g2d.drawRect(i*blockSize,j*blockSize,blockWidth,blockWidth);
                                g2d.setColor(QREditor.simbolC[j][i]);
                                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,QREditor.simbolT[j][i]));
                                g2d.fillRect(i*blockSize,j*blockSize,blockWidth,blockWidth);
                                break;
                        case DATA_0:
                                //データ0
                                g2d.setColor(Color.BLACK);
                                g2d.drawRect(i*blockSize,j*blockSize,blockWidth,blockWidth);
                                g2d.setColor(QREditor.simbolC[j][i]);
                                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,QREditor.simbolT[j][i]));
                                g2d.fillRect(i*blockSize,j*blockSize,blockWidth,blockWidth);
                                //System.out.println(QREditor.simbolC[j][i].getRed());
                                break;
                        case DATA_VER:
                                //型番情報
                                g2d.setColor(Color.GREEN);
                                g2d.drawRect(i*blockSize,j*blockSize,blockWidth,blockWidth);
                                g2d.setColor(DATA_VER_COLOR);
                                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,QREditor.colorTran));
                                g2d.fillRect(i*blockSize,j*blockSize,blockWidth,blockWidth);
                                break;
                        case DATA_K:
                                //型番情報
                                g2d.setColor(Color.GRAY);
                                g2d.drawRect(i*blockSize,j*blockSize,blockWidth,blockWidth);
                                g2d.setColor(DATA_K_COLOR);
                                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,QREditor.colorTran));
                                g2d.fillRect(i*blockSize,j*blockSize,blockWidth,blockWidth);
                                break;
                        case DATA_E1:
                                //エディタ固定データ1
                                QREditor.simbolC[j][i] = DATA_E_COLOR;
                                //QREditor.simbolT[j][i] = tranValue;
                                g2d.setColor(Color.MAGENTA);
                                g2d.drawRect(i*blockSize,j*blockSize,blockWidth,blockWidth);
                                g2d.setColor(DATA_E_COLOR);
                                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,QREditor.colorTran));
                                g2d.fillRect(i*blockSize,j*blockSize,blockWidth,blockWidth);
                                break;
                        case DATA_E0:
                                //エディタ固定データ0
                                QREditor.simbolC[j][i] = BACKGROUND_COLOR;
                                //QREditor.simbolT[j][i] = tranValue;
                                g2d.setColor(Color.MAGENTA);
                                g2d.drawRect(i*blockSize,j*blockSize,blockWidth,blockWidth);
                                g2d.setColor(BACKGROUND_COLOR);
                                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.3f));
                                g2d.fillRect(i*blockSize,j*blockSize,blockWidth,blockWidth);
                                break;
                        default:
                                //エラー
                                System.out.println("エラー：　シンボルのデータがおかしいです。　："+QREditor.simbol[i][j]);
                                QRCode.info.setText(QRCode.info.getText()+"エラー：　シンボルのデータがおかしいです。　："+QREditor.simbol[i][j]+BR);
                    }
                }
            }
        }
        private void drawImage(Graphics g)
        {
            if(imgVisible && QREditor.readImage!=null){
                Graphics2D g2d = (Graphics2D)g;
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,QREditor.imgTran));
                g2d.drawImage(QREditor.readImage, imageX, imageY, this);
            }
        }
        private void drawFilter(Graphics g)
        {
            //未定義
        }
	public void Clicked(int x,int y,int paintMode){
		if(x>=0 && y>=0 && x<=QREditor.simSize*(blockWidth+quiet) && y<=QREditor.simSize*(blockWidth+quiet)){
			x=(int)x/(blockWidth+quiet);
			y=(int)y/(blockWidth+quiet);
			if(nowX!=x || nowY!=y){
				nowX=x;
				nowY=y;
				if((QREditor.simbol[y][x]==DATA_0||QREditor.simbol[y][x]==DATA_1) && paintMode==1){
					//消しゴム&両方
					QREditor.simbol[y][x]=DATA_0;
                                        QREditor.simbolC[y][x] = BACKGROUND_COLOR;
                                        QREditor.simbolT[y][x] = 0.0f;
				}else if((QREditor.simbol[y][x]==DATA_0||QREditor.simbol[y][x]==DATA_1) && paintMode==0){
					//鉛筆&両方
                                    if(briV<regList)
                                    {
                                        QREditor.simbol[y][x]=DATA_1;
                                        QREditor.simbolC[y][x] = rgbValue;
                                        QREditor.simbolT[y][x] = tranValue;
                                    }else if(briV>regList)
                                    {
                                        QREditor.simbol[y][x]=DATA_0;
                                        QREditor.simbolC[y][x] = rgbValue;
                                        QREditor.simbolT[y][x] = tranValue;
                                    }
				}else if(QREditor.simbol[y][x]==DATA_0 && paintMode==3){
					//塗りつぶし
				}else if(QREditor.simbol[y][x]==NO_DATA&&briV<regList && paintMode==2){
					NO_DATA_COLOR = rgbValue;
				}
                                else if(QREditor.simbol[y][x]==BLACK_DATA&&briV<regList && paintMode==2){
					BLACK_DATA_COLOR = rgbValue;
				}
                                else if(QREditor.simbol[y][x]==K_DATA && paintMode==2){
					//未定義
				}
                                else if(QREditor.simbol[y][x]==DATA_VER&&briV<regList && paintMode==2){
					DATA_VER_COLOR = rgbValue;
				}
                                else if(QREditor.simbol[y][x]==DATA_K){
					DATA_K_COLOR = rgbValue;
				}
                                else if(QREditor.simbol[y][x]==DATA_E1&&briV<regList && paintMode==2){
					DATA_E_COLOR = rgbValue;
				}
                                /*else if((QREditor.simbol[y][x]==WHITE_DATA||QREditor.simbol[y][x]==DATA_E0)&&briV>regList && paintMode==2){
                                        BACKGROUND_COLOR = rgbValue;
				}*/
                                                //未定義
				repaint();
			}
		}else{
			System.out.println("枠外です");
		}
	}
	public void Released(){
                nowX=-1;
		nowY=-1;
	}
	static void bitTurn(int x,int y){
		if(x>=0 && x<QREditor.simSize && y>=0 && y<QREditor.simSize){
			if(QREditor.simbol[y][x]==DATA_0){
				QREditor.simbol[y][x]=DATA_1;
			}else if(QREditor.simbol[y][x]==DATA_1){
				QREditor.simbol[y][x]=DATA_0;
			}
		}
	}
}