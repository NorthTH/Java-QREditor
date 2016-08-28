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
	////////////////////////////////////////////////�t�B�[���h//////////////////////////////////////////
	//DataBase����f�[�^�擾
		public static final int NO_DATA = DataBase.NO_DATA;					//�萔(�f�[�^��)
		public static final int BLACK_DATA = DataBase.BLACK_DATA;			//�萔(���f�[�^)
		public static final int WHITE_DATA = DataBase.WHITE_DATA;			//�萔(���f�[�^)
		public static final int K_DATA = DataBase.K_DATA;					//�萔(�`���A�^�ԗ\��)
		public static final int DATA_1 = DataBase.DATA_1;					//�萔(�R�[�h�f�[�^1)
		public static final int DATA_0 = DataBase.DATA_0;					//�萔(�R�[�h�f�[�^0)
		public static final int DATA_VER = DataBase.DATA_VER;				//�萔(�^�ԏ��)
		public static final int DATA_K = DataBase.DATA_K;					//�萔(�`�����)
		public static final Color TITLE_GRAY = DataBase.TITLE_GRAY;			//�萔(�^�C�g����ʔw�i�F)
		public static final Color TITLE_GRAY2 = DataBase.TITLE_GRAY2;		//�萔(�^�C�g����ʔw�i�F2)
		public static final String BR = DataBase.BR;						//�萔(���s�R�[�h)
		public static final String GRAP_SEPARATOR=DataBase.GRAP_SEPARATOR;	//�萔(�O���t�B�b�N���̕��������i�[)
		public static final String BR_IO=DataBase.BR_IO;					//�萔(�t�@�C�����o�͎��̉��s�R�[�h�̒u����������)
		int[][][] versionMaxLen=DataBase.getVersionMaxLen();				//QR�R�[�h�d�lDB
		char[] eisuutaiou=DataBase.eisuutaiou;								//�p�������[�h�̕����Ή��\DB
		int[] dataA=DataBase.dataA;											//GF(2^8)�̎w���\���̑Ή�DB
	//�t�@�C���̓��o��
		JFileChooser chooser = new JFileChooser();							//�t�@�C���̓��o��
	//GUI�I�u�W�F�N�g�錾
		JLabel StatusBar;													//�X�e�[�^�X�o�[���ǂ�
		JButton b;															//�ϊ��{�^��
		JButton inB;														//�J���{�^��
		JButton editorB;													//�G�f�B�^���J���{�^��
		JButton clearB;
                public static JTextArea inputD;											//���̓G���A
		static JTextArea info;												//���\���G���A
		JScrollPane inputDS;												//���̓G���A�̃X�N���[���o�[
		JScrollPane infoS;													//���\���G���A�̃X�N���[���o�[
		static JComboBox verList;											//�^�ԃ��X�g
		static JComboBox maskList;											//�}�X�N���X�g
		static JComboBox lvList;											//�������x�����X�g
		static JComboBox sizeList;
                QRShow[] qrs=new QRShow[simbolNumMax];								//QR�R�[�h�\���I�u�W�F�N�g
		QREditor qre;														//�G�f�B�^�I�u�W�F�N�g
		ImageIcon icon = new ImageIcon("icon.png");							//�A�C�R���̐ݒ�
	//���j���[�o�[
		MenuItem NewFile	= new MenuItem( "�V�K�쐬" );					//[�t�@�C��]->[�V�K�쐬]
		MenuItem Open		= new MenuItem( "�J��" );						//[�t�@�C��]->[�J��]
		MenuItem Exit		= new MenuItem( "�v���O�����̏I��" );			//[�t�@�C��]->[�v���O�����̏I��]
		MenuItem Editor		= new MenuItem( "�G�f�B�^�̋N��" );				//[�ҏW]->[�G�f�B�^�̋N��]
		MenuItem Do		= new MenuItem( "�ϊ�" );						//[�ҏW]->[�ϊ�]
		MenuItem ClearInput	= new MenuItem( "���̓G���A�̃N���A" );			//[�\��]->[���̓G���A�̃N���A]
		MenuItem ClearInfo	= new MenuItem( "���G���A�̃N���A" );			//[�\��]->[���G���A�̃N���A]
		MenuItem Version	= new MenuItem( "�o�[�W�������" );				//[�w���v]->[�o�[�W�������]
	//���̑�
		int WindowSizeX=450;												//�E�B���h�E�T�C�Y(X)
		int WindowSizeY=770;												//�E�B���h�E�T�C�Y(Y)
		float version=(float)0.7;											//�v���O�����̃o�[�W�������i�[
		static int simbolNumMax=999;										//�V���{���\���I�u�W�F�N�g�̍ō���
		boolean coop=false;													//�O���t�B�b�N�����ߍ��܂�Ă��邩�ǂ���
		int count=0;														//�ꎞ�I�ȕϐ�
		String Title="QR-Code Maker";										//�v���O�����̃^�C�g��
		String[] dataBit;													//�f�[�^�r�b�g����i�[
		String Inputdata;													//���͂��ꂽ�f�[�^���i�[
		String[] InputdataDiv=new String[2];								//���̓f�[�^���f�[�^���ƃO���t�B�b�N���ɕ����Ċi�[
		String SelectedLv="L(7%)";											//�I�����ꂽ�������x�����e�L�X�g�Ŋi�[
		String SelectedMode;												//�I�����ꂽ�G���R�[�h���[�h���e�L�X�g�Ŋi�[
		int verMax=40;														//�ő�^��
		static int ver=0;													//�I�����ꂽ�^�Ԃ��i�[ 0�`40
		static int lv=0;													//�I�����ꂽ�������x�����i�[ 0�`3
		static int encodeMode;												//�G���R�[�h���[�h���i�[ 0�`3
		int mozisusizisiLen;												//�������w���q�̒������i�[
		int souCodeGosu=0;													//���R�[�h�ꐔ���i�[
		int errorTeiseiSu=0;												//�G���[���������i�[
		String[][] errorF;													//�������������i�[����z��
		int errorTS;														//������
		int[] g;															//������
		int[] f;															//������
		String[] f2;														//������
		String[] dtmp;														//������
		String[][] dataB;													//�C���^���[�u�z�u�̊e�f�[�^�r�b�g����i�[
		int RSSum=1;														//RS�u���b�N���̍��v
		int dBkazu;															//RS�u���b�N���������̎��ɃR�[�h���̌v�Z�Ɏg�p
		int simbolNum=0;													//�V���{���̐����ԍ�
		public static int[][] simbol;										//�V���{�����i�[
		public static int simSize=0;										//�V���{���̃T�C�Y���i�[
		static int mask=0;													//�K�p����}�X�N�̎�ނ��i�[
		int[][] currentSimbol;												//�}�X�N�̕]��������ꍇ�̊e�V���{�����i�[
		int score;															//�V���{���̕]���̎��_���i�[
		String modesikibetusi="0000";										//���[�h���ʎq���i�[
		String mozisusizisi=null;											//�������w���q���i�[
		static BufferedImage readImage=null;
		static int imageX=0;
		static int imageY=0;
                
                static int initImageSize=10;
                static int imageSize=0;

	////////////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////�R���X�g���N�^////////////////////////////////////////
	QRCode(){
	//���j���[
		MenuBar MyMenu = new MenuBar();
		Menu FileMenu = new Menu( "�t�@�C��" );
  		  	NewFile.addActionListener(this);
    		Open.addActionListener(this);
    		Exit.addActionListener(this);
			FileMenu.add( NewFile );
			FileMenu.add( Open );
			FileMenu.addSeparator();
			FileMenu.add( Exit );
		Menu EditMenu = new Menu( "�ҏW" );
  		  	Editor.addActionListener(this);
  		  	Do.addActionListener(this);
			EditMenu.add(Editor);
			EditMenu.addSeparator();
			EditMenu.add(Do);
		Menu ShowMenu = new Menu( "�\��" );
  		  	ClearInput.addActionListener(this);
  		  	ClearInfo.addActionListener(this);
			ShowMenu.add(ClearInput);
			ShowMenu.add(ClearInfo);
		Menu HelpMenu = new Menu( "�w���v" );
  		  	Version.addActionListener(this);
			HelpMenu.add(Version);
		MyMenu.add( FileMenu );
		MyMenu.add( EditMenu );
		MyMenu.add( ShowMenu );
		MyMenu.add( HelpMenu );
		setMenuBar(MyMenu);

	//�X�e�[�^�X�o�[
		StatusBar=new JLabel("Version:"+version);
	//�{�^��
		b=new JButton("�ϊ�");
		b.addActionListener(this);
	//�{�^��(�J��)
		inB=new JButton("�t�@�C�����J��");
		inB.addActionListener(this);
	//�{�^��(�J��)
		editorB=new JButton("�G�f�B�^���J��");
		editorB.addActionListener(this);
	//�{�^��(�J��)
		clearB=new JButton("�N���A");
		clearB.addActionListener(this);
        //�e�L�X�g�G���A
		//���̓G���A
			inputD=new JTextArea("", 4,38);
			inputD.setLineWrap(true);
			inputDS=new JScrollPane(inputD);
			inputDS.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			inputDS.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			inputDS.setPreferredSize(new Dimension(WindowSizeX-25, 140));
		//���G���A
			info=new JTextArea(Title+BR+BR, 4, 30);
			infoS=new JScrollPane(info);
			infoS.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			infoS.setPreferredSize(new Dimension(WindowSizeX-25, 200));
	//�R���{�{�b�N�X
		//�������x��
			lvList = new JComboBox();
			lvList.addItem(" L ( 7%)");
			lvList.addItem(" M (15%)");
			lvList.addItem(" Q (25%)");
			lvList.addItem(" H (30%)");
			lvList.setLightWeightPopupEnabled(false);	//�d�ʃR���|�[�l���g�����ăv���_�E�����\������Ȃ��s�����
			lvList.addActionListener(this);
		//�^��
			verList = new JComboBox();
			verList.addItem("Auto");
			for(int i=1;i<=verMax;i++){
				verList.addItem(Integer.toString(i));
			}
			verList.setLightWeightPopupEnabled(false);	//�d�ʃR���|�[�l���g�����ăv���_�E�����\������Ȃ��s�����
			verList.addActionListener(this);
		//�}�X�N
			maskList = new JComboBox();
			maskList.addItem("Auto");
			for(int i=0;i<8;i++){
				maskList.addItem(Integer.toString(i));
			}
			maskList.setLightWeightPopupEnabled(false);	//�d�ʃR���|�[�l���g�����ăv���_�E�����\������Ȃ��s�����
			maskList.addActionListener(this);
                //�T�C�Y
                        sizeList = new JComboBox();
			sizeList.addItem("1");
			sizeList.addItem("2");
			sizeList.addItem("3");
			sizeList.addItem("4");
                        sizeList.addItem("5");
                        sizeList.addItem("6");
			sizeList.setLightWeightPopupEnabled(false);	//�d�ʃR���|�[�l���g�����ăv���_�E�����\������Ȃ��s�����
			sizeList.addActionListener(this);
	//���C�A�E�g
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
		p4.add(new JLabel("QR�R�[�h������f�[�^"));
		p5.add(inputDS);
		p7.add(new JLabel("����"));
		p7.add(lvList);
		p7.add(new JLabel("�@�^��"));
		p7.add(verList);
		p7.add(new JLabel("�@�}�X�N"));
		p7.add(maskList);
                p7.add(new JLabel("�@�T�C�Y"));
		p7.add(sizeList);
		p7.add(new JLabel("�@�@"));
		p7.add(b);
                p7.add(clearB);
		p9.setLayout(new FlowLayout(FlowLayout.LEFT));
		p9.add(new JLabel("���G���A"));
		p10.add(infoS);
		//p11.setBackground();
		p11.setLayout(new FlowLayout(FlowLayout.LEFT));
		p11.add(StatusBar,BorderLayout.SOUTH);
		setIconImage(icon.getImage());

		//�I������
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//�\��
			init();
			setTitle(Title);
			setSize(WindowSizeX, WindowSizeY);
			setVisible(true);
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////�C�x���g���\�b�h////////////////////////////////////////
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		//�{�^��(�ϊ�)�̃C�x���g
			if(obj==b || obj==Do){
				QRSyori();
				if(simbolNum<simbolNumMax){
					int qrsWidth=simSize*QRShow.blockWidth+(QRShow.quiet*2);
					qrs[simbolNum]=new QRShow(simbolNum,simbol,simSize,readImage,imageX,imageY);
					qrs[simbolNum].setTitle(""+ver+SelectedLv.charAt(0)+"�^ �F"+inputD.getText());
					qrs[simbolNum].setLocation(getX()+(getWidth()/2)-(qrsWidth/2), getY()+(getHeight()/2)-(qrsWidth/2));
					simbolNum++;
					ver=verList.getSelectedIndex();
					//readImage=null;
				}else{
					System.out.println("�G���[�F�@����ȏ�V���{���𐶐��ł��܂���B");
					info.setText(info.getText()+"�G���[�F�@����ȏ�V���{���𐶐��ł��܂���B"+BR);
				}
			}
		//�{�^��(�G�f�B�^���J��)�̃C�x���g
			if(obj==editorB || obj==Editor){
				qre=new QREditor();
				qre.setLocation(getX()+getWidth(), getY());
			}
                /*//�{�^��(�N���A)�̃C�x���g
                        if(obj==clearB || obj==Clear){
				inputD.setEditable(true);
                                inputD.setText("");
                                readImage = null;
			}*/
		//�{�^��(�J��)�̃C�x���g
			if(obj==inB || obj==Open){
				int returnVal = chooser.showOpenDialog(this);
				try {
					if (returnVal == JFileChooser.APPROVE_OPTION) {	// OK�{�^���������ꂽ���̏���
						File file = chooser.getSelectedFile();
						BufferedReader in  = new BufferedReader(new FileReader(file));
						boolean ioError=false;
						int simSizeTmp=0;
						int[][] simbolTmp=new int[simSizeTmp][simSizeTmp];
						String str1=null;
						String str2=null;
						//�^�ԁA���[�h�A�������x���A�}�X�N�擾
							String[] ioData;
							ioData=in.readLine().split(",");
							if(ioData.length!=4){
								ioError=true;
							}
						simError:
						if(!ioError){
							//�V���{���擾
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
							//���̓f�[�^�A�o�̓f�[�^�擾
								str1=in.readLine();
								str2=in.readLine();
								if(str2.indexOf(str1+GRAP_SEPARATOR)==-1){
									ioError=true;
								}
						}
						//�G���[����
							if(ioError){
								DataBase.ioError();
								JOptionPane.showMessageDialog(this, "�t�@�C���̓ǂݍ��݂Ɏ��s���܂����B","�G���[", JOptionPane.ERROR_MESSAGE);
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
		//�R���{�{�b�N�X(�^��)�̃C�x���g
			if(obj==verList){
				ver=verList.getSelectedIndex();
			}
		//�R���{�{�b�N�X(�}�X�N)�̃C�x���g
			if(obj==maskList){
				mask=maskList.getSelectedIndex();
			}
		//�R���{�{�b�N�X(�������x��)�̃C�x���g
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
		//�V�K�쐬�̃C�x���g
			if(obj==NewFile){
				init();
			}
		//�v���O�����̏I���̃C�x���g
			if(obj==Exit){
				System.exit(0);
			}
		//���G���A�̃N���A�C�x���g
			if(obj==clearB || obj==ClearInput){
				inputD.setEditable(true);
                                inputD.setText("");
                                readImage = null;
			}
		//���G���A�̃N���A�C�x���g
			if(obj==ClearInfo){
				info.setText("");
			}
		//�o�[�W�������̃C�x���g
			if(obj==Version){
				JOptionPane.showMessageDialog(this, "�o�[�W����:"+version,"�o�[�W�������", JOptionPane.PLAIN_MESSAGE);
			}
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////���C�����\�b�h////////////////////////////////////////
	public static void main(String[] args) {
		QRCode qr=new QRCode();
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////QR�R�[�h�v�Z�������\�b�h///////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	public void QRSyori(){
		Inputdata=inputD.getText();
		/////////////////////////////////////////////////////////////
		///////////////�O���t�B�b�N�����ߍ��܂�Ă��邩//////////////
		int GrapIndex=Inputdata.lastIndexOf(GRAP_SEPARATOR);
			if(GrapIndex==-1){
				//�O���t�B�b�N�f�[�^���Ȃ����̏���
				coop=false;
				InputdataDiv[0]=Inputdata;
			}else{
				//�O���t�B�b�N�f�[�^�����鎞�̏���
				coop=true;
				encodeMode=2;
				InputdataDiv[0]=Inputdata.substring(0,GrapIndex);
				InputdataDiv[1]=Inputdata.substring(GrapIndex+GRAP_SEPARATOR.length());
		}

		//////////////////////////////////////////////////////////////
		//////////////////////////���[�h����//////////////////////////
			encodeMode=DataBase.getEncodeMode(InputdataDiv[0]);
			switch(encodeMode){
				case 0:
					SelectedMode="����";
					break;
				case 1:
					SelectedMode="�p����";
					break;
				case 2:
					SelectedMode="8bit�o�C�g";
					break;
				case 3:
					SelectedMode="����";
					break;
				default:
					//�G���[
					SelectedMode="�s��";
					System.out.println("�G���[�F�@�G���R�[�h���[�h�̒l���ُ�ł��B�@�F"+encodeMode);
					info.setText(info.getText()+"�G���[�F�@�G���R�[�h���[�h�̒l���ُ�ł��B�@�F"+encodeMode+BR);
			}
			//�G�f�B�^�Ŏ����Ń��[�h������ł���悤�ɂȂ�΁��폜
				/*if(coop){
					encodeMode=2;
				}
				*/
		/////////////////////////////////////////////////////////////
		////////////////���肳�ꂽ���[�h�̌^�ԕʗe�ʂ��擾///////////
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
		//////////////////////�o�[�W�������肪�����̎�/////////////////
		if(ver==0){
			for(int i=1;i<41;i++){
				if(Inputdata.length()<=versionMaxLen[i][lv][encodeMode]){
					ver=i;
					break;
				}
			}
		}
		//�f�o�b�O�p
			System.out.println("���͕�����="+Inputdata.length()+"�A�^��"+ver+"�̋��e������="+versionMaxLen[ver][lv][encodeMode]);

		//////////////////////////////////////////////////////////////
		//////////////////////////�������w���q//////////////////////////
		mozisusizisiLen=DataBase.mozisusizisiLenGet(ver,encodeMode);
		mozisusizisi = DataBase.get2(InputdataDiv[0].length(), mozisusizisiLen);

		//////////////////////////////////////////////////////////////
		//////////////////////////���[�h�w���q���擾////////////////////
		modesikibetusi=DataBase.getMode(encodeMode);

		//////////////////////////////////////////////////////////////
		//////////////////////////�f�[�^�̕�����//////////////////////
		InputdataDiv[0]=DataBase.DtoB(InputdataDiv[0],encodeMode);

		//////////////////////////////////////////////////////////////
		////////////////////////�I�[�p�^�[���}��///////////////////////
		String tmpS=null;
		int terminatorLen=0;
		tmpS = modesikibetusi+mozisusizisi+InputdataDiv[0];
		//�f�o�b�O�p
			System.out.println("�f�[�^�̗�="+tmpS.length()+"�@���E��="+((versionMaxLen[ver][lv][4]*8)));
		String terminator="";
		terminatorLen=(versionMaxLen[ver][lv][4]*8)-tmpS.length();
		if(terminatorLen>3){
			terminator = "0000";
		}else{
			for(int i=0;i<terminatorLen;i++){
				terminator+="0";
				//�f�o�b�O�p
					System.out.println("�I�[�p�^�[���}����="+terminatorLen);
			}
		}
		tmpS +=terminator;
		////////////////////////////////////////////////////////////////
		/////////////////8bit���ɋ�؂�A���ߑ�bit�}��/////////////////
		//�f�[�^�r�b�g���i�[����z��̐���
			dataBit=new String[versionMaxLen[ver][lv][4]+versionMaxLen[ver][lv][5]];
		int nowPlace=0;
		//�O���t�B�b�N������΃f�[�^����
			if(coop){
				tmpS+=InputdataDiv[1];
			}
		//8bit�P�ʂ�dataBit�z��Ƀf�[�^���i�[
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
			//�Ō��bit��8bit�ɂȂ�悤���ߑ�bit�}��(�O���t�B�b�N�Ȃ��ꍇ)
				for (int j=0; dataBit[nowPlace-1].length()<8; j++) {
						dataBit[nowPlace-1] += "0";
				}
			//���ߑ�bit����f�[�^�R�[�h���𖞂����܂ő}��(�O���t�B�b�N�Ȃ��ꍇ)
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
		//�f�o�b�O�p
			System.out.println("[�f�[�^�r�b�g��̕\��]");
			for (int j=0; j<versionMaxLen[ver][lv][4]; j++) {
				System.out.println("dataBit["+j+"] ="+dataBit[j]+"=>"+DataBase.get10(dataBit[j]));
			}

		//�f�[�^��10�i���ɕϊ�
			for (int i=0; i<versionMaxLen[ver][lv][4]; i++) {
				dataBit[i] = Integer.toString(DataBase.get10(dataBit[i]));
			}

		//////////////////////////////////////////////////////////////
		////////////////////////������//////////////////////////////
		//���R�[�h�ꐔ���v�Z
			souCodeGosu = versionMaxLen[ver][lv][4]+versionMaxLen[ver][lv][5];
		//RS�u���b�N���̍��v���v�Z
			RSSum=versionMaxLen[ver][lv][6]+versionMaxLen[ver][lv][7];
		//�G���[���������v�Z
			errorTeiseiSu = versionMaxLen[ver][lv][5]/2;
			if (ver == 1 && lv == 0) {
				//1L�̎���2
				errorTeiseiSu = 2;
			} else if (ver == 1 && lv == 1) {
				//1M�̎���4
				errorTeiseiSu = 4;
			} else if (ver == 2 && lv == 0) {
				//2L�̎���4
				errorTeiseiSu = 4;
			} else if (ver == 1 && lv == 3) {
				//1H�̎���8
				errorTeiseiSu = 8;
			}
		//�e�u���b�N�̌������R�[�h�����擾
			errorTS=versionMaxLen[ver][lv][5];
			errorTS=errorTS/RSSum;
		//����������g���擾
			G(errorTS);
		//RS�u���b�N��1�̎��̌�����
			if (RSSum == 1) {
				errorF=new String[1][errorTS];
				errorCode(dataBit,errorTS,errorF[0],versionMaxLen[ver][lv][4]);	
				//�f�[�^��z�u���鏇�Ƀ\�[�g
					for(int i=0;i<errorF[0].length;i++){
						dataBit[versionMaxLen[ver][lv][4]+i]=errorF[0][i];
					}
				//�f�o�b�O�p
					for(int i=0;i<dataBit.length;i++){
						//System.out.println("dataBit["+i+"]= "+dataBit[i]);
					}
			}
		//RS�u���b�N����2�ȏ�̎��̌�����
			if(RSSum>1){
				//�f�o�b�O�p
					System.out.println("RS�u���b�N��2�ȏ�");
				//�f�[�^�u���b�N�̔z��𐶐�
					 dataB=new String[RSSum][];
				//�P�߂�RS�u���b�N��
					count=0;
					for(int i=0;i<versionMaxLen[ver][lv][6];i++){
						dBkazu=(int)(versionMaxLen[ver][lv][4]/RSSum);
						dataB[i]=new String[dBkazu];
						for(int j=0;j<dBkazu;j++){
							dataB[i][j]=dataBit[count];
							count++;
						//�f�o�b�O�p
								System.out.println("dataB["+i+"]["+j+"]="+dataB[i][j]+"�@���@"+DataBase.get2(Integer.parseInt(dataB[i][j]),10));
						}
					}
				//2�߂�RS�u���b�N��
					for(int i=0;i<versionMaxLen[ver][lv][7];i++){
						dBkazu=(int)(versionMaxLen[ver][lv][4]/RSSum)+1;
						dataB[i+versionMaxLen[ver][lv][6]]=new String[dBkazu];
						for(int j=0;j<dBkazu;j++){
							dataB[i+versionMaxLen[ver][lv][6]][j]=dataBit[count];
							count++;
							//�f�o�b�O�p
								System.out.println("dataB["+(i+versionMaxLen[ver][lv][6])+"]["+j+"]="+dataB[i+versionMaxLen[ver][lv][6]][j]+"�@���@"+DataBase.get2(Integer.parseInt(dataB[i+versionMaxLen[ver][lv][6]][j]),10));
						}
					}

				//���ꂼ��̃f�[�^�u���b�N�̌������������v�Z
					//�������������i�[����z��̐���
						errorF=new String[RSSum][errorTS];
					for(int i=0;i<RSSum;i++){
						errorCode(dataB[i],errorTS,errorF[i],dataB[i].length);
					}
				//�f�[�^��z�u���鏇�Ƀ\�[�g
					dataBit=new String[versionMaxLen[ver][lv][4]+errorTS*RSSum];
				//�f�[�^�R�[�h�������\�[�g
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
				//�����������������\�[�g
					for(int i=0;i<errorTS;i++){
						for(int j=0;j<RSSum;j++){
							dataBit[count]=errorF[j][i];
							count++;
						}
					}
			}

			//�f�o�b�O�p
				for(int i=0;i<dataBit.length;i++){
					//System.out.println("�ŏIdataBit["+i+"]= "+dataBit[i]);
				}
		//////////////////////////////////////////////////////////////
		//////////////////////�f�[�^�̂Q�i��//////////////////////////
			for (int i=0; i<dataBit.length; i++) {
				dataBit[i]=DataBase.get2(Integer.parseInt(dataBit[i]),8);
			}
	////////////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////QR�R�[�h�\������//////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////

		//////////////////////////////////////////////////////////////
		/////////////////�V���{���̐����A�����ݒ�///////////////////////
		//�V���{���𐶐�
			simSize = 21+(4*(ver-1));
			simbol = new int[simSize][simSize];
		//�V���{���ɌŒ�f�[�^�̖��ߍ���
			DataBase.Indata(ver,simSize,simbol);

		//////////////////////////////////////////////////////////////
		///////////////////�V���{���Ƀf�[�^�̔z�u/////////////////////
		//�����ݒ�
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
		  //�z�u
			haitiBitLength=haitiBit.length;
			int compNum=0;
			for(int i=0;compNum<haitiBit.length;i++){
				count=0;
				//�f�o�b�O�p
					if(nowX<-1){
						System.out.println("�G���[�F�@�f�[�^���V���{���Ɏ��܂肫��܂���@�@�c��r�b�g���F"+(haitiBit.length-i-1));
						info.setText(info.getText()+"�G���[�F�@�f�[�^���V���{���Ɏ��܂肫��܂���@�@�c��r�b�g���F"+(haitiBit.length-i-1)+BR);
					}
				if(nowX>=0 && nowX<=simSize-1 && nowY>=0 && nowY<=simSize-1){
					if(simbol[nowY][nowX]==0){
						//�f�[�^����Ȃ�r�b�g��z�u
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
					//�ړ�
						if(LeftOrRight==1){
							//�E��
							//�f�o�b�O�p
								//System.out.println("�E��		"+nowX+",	"+nowY);
							nowX--;
							LeftOrRight=-LeftOrRight;
						}else if(nowY>=0 && nowY<=simSize-1){
							//�f�o�b�O�p
								//System.out.println("�㉺�ړ��\	"+nowX+",	"+nowY);
							nowY-=direct;
							nowX++;
							LeftOrRight=-LeftOrRight;
						}else{
							//�f�o�b�O�p
								//System.out.println("�㉺���E	"+nowX+",	"+nowY);
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
		//////////////////////////�}�X�N����///////////////////////////
		int Maxscore=99999;
		currentSimbol=new int[simSize][];
		//�e�}�X�N�̕]��
			if(mask==0){
				for(int i=0;i<8;i++){
					//�V���{���̃R�s�[
						for(int j=0;j<simSize;j++){
							currentSimbol[j]=(int[])simbol[j].clone();
						}
					//�}�X�N�̓K�p
						DataBase.mask(currentSimbol,i,simSize,0);
					//�`�����̖��ߍ���
						DataBase.setFormatInformation(currentSimbol,lv,i);
					//�V���{���̕]��
						System.out.print("Mask;"+i+"�@");
						score=CheckSimbol(currentSimbol);
					//�}�X�N�̌���
						if(Maxscore>score){
							mask=i;
							Maxscore=score;
						}
				}
			}else{
				mask-=1;
				score=CheckSimbol(simbol);
			}
		//�V���{���̌���
				System.out.println("�̗p���ꂽ�}�X�N="+mask);
			//�}�X�N�̓K�p
				if(coop){
					DataBase.mask(simbol,mask,simSize,0);
				}else{
					DataBase.mask(simbol,mask,simSize,0);
				}
			//�`�����̖��ߍ���
				DataBase.setFormatInformation(simbol,lv,mask);
		//�f�o�b�O�p
			//DataBase.sTrace(simbol,simSize,"�f�[�^�z�u");

		//�I��
			info.setText(info.getText()+"�^��="+ver+"�@�������x��="+SelectedLv+"�@�}�X�N="+mask+"�@���_="+score+"�@�G���R�[�h���[�h="+SelectedMode+BR);
			info.setText(info.getText()+"QR�������I�����܂����B"+BR+"========================================"+BR);
			mask=maskList.getSelectedIndex();
	}


	/////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////���\�b�h//////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////

	//////////////////////////////////////////////////////////////
	////////////GF(256)�̃��𐮐��ɕϊ����郁�\�b�h///////////////
	public int ahenkan(int a){
		if(a<1 || a>255){
			return 0;
		}
		return dataA[a-1];	
	}

	//////////////////////////////////////////////////////////////
	////////////GF(256)�̐��������ɕϊ����郁�\�b�h///////////////
	public int agyakuhenkan(int a){
		for(int iii=0;iii<255;iii++){
			if(a==dataA[iii]){
				return iii+1;
			}else if(a==255){
				return 1;
			}
		}
		System.out.println("�G���[�F�@agyakuhenkan()�ɂ�"+a);
		info.setText(info.getText()+"�G���[�F�@agyakuhenkan()�ɂĕϊ��ł��Ȃ����������͂���܂����B( 1�`255) a="+a+BR); 
		return 0;
	}
	//////////////////////////////////////////////////////////////
	///////////////////�ׂ�����v�Z���郁�\�b�h///////////////////
	public int beki(int a,int b){
		int c=1;
		for(int iii=0;iii<b;iii++){
			c*=a;
		}
		return c;
	}

	//////////////////////////////////////////////////////////////
	///////////////////���������������߂郁�\�b�h/////////////////
	public int[] G(int a){
		//����������g(x)�����߂�
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
				//���̕����̒�`���s��
				//
				//	
				default:
					System.out.println("�G���[�F�@�������R�[�h�ꐔ���ُ�ŁA����������g(x)�����܂���@�F"+a);
					info.setText(info.getText()+"�G���[�F�@�������R�[�h�ꐔ���ُ�ŁA����������g(x)�����܂���@�F"+a+BR);
			}
		return g;
	}

	//////////////////////////////////////////////////////////////
	////////////////�������������v�Z���郁�\�b�h////////////////
	public void	errorCode (String a[],int b,String c[],int d){

		//�e�z��𐶐��A������
			f = new int[d+errorTS];
			f2 =new String[d+errorTS];
		//�e�z��ɏ����l0������
			for (int i=0; i<d+errorTS; i++) {
				f2[i]="0";
				f[i]=0;
			}
		//f2�z���dataBit���R�s�[
			for (int i=0; i<d; i++) {
				f2[i] = a[i];
			}
			//�f�o�b�O�p
			/*
				for(int i=0;i<f2.length;i++){
					System.out.println("f2["+i+"]="+f2[i]);
				}
			*/
		//�����������̌v�Z
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
		//������������z��Ɋi�[
			for (int i=0; i<b; i++) {
				c[i] = f2[d+i];
			}
		//�f�o�b�O�p
			/*
			for(int i=0;i<f2.length;i++){
				System.out.println("f2["+i+"]="+f2[i]);
			}
			System.out.println("===========================================");
			for(int i=0;i<f.length;i++){
				System.out.println("f["+i+"]="+f[i]);
			}
			System.out.println("[������������\��]");
			for (int i=0; i<b; i++) {
				System.out.println("c["+i+"] ="+c[i]);
			}
			*/
	}

	///////////////////////////////////////////////////////////////////
	////////////////////�V���{���̕]�������郁�\�b�h///////////////////	
	public int CheckSimbol(int a[][]){
	int score=0;
	int countX=0;
	int countY=0;
	boolean colorX=false;
	boolean colorY=false;
	//�V���{�������Ɣ��ɂ����z���p��
		boolean[][] b=new boolean[simSize][simSize];
		b=simbolToBoolean(a);
		//�f�o�b�O�p
			//DataBase.sTrace(b,simSize,"�f�o�b�O�p");
		//���F�̍s�E��̗אڃ��W���[��
			//�s
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
							//System.out.println("���F�̍s�̗אڃ��W���[�� s["+i+"]["+(j-countX)+"]-s["+i+"]["+(j-1)+"]�F-"+(countX-2));
							score+=countX-2;
						}
						countX=1;
						colorX=b[j][i];
					}
				}
			}
			//��
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
							//System.out.println("���F�̗�̗אڃ��W���[�� s["+(j-countY)+"]["+i+"]-s["+(j-1)+"]["+i+"]�F-"+(countY-2));
							score+=countY-2;
						}
						countY=1;
						colorY=b[i][j];
					}
				}
			}
		//���F�̃��W���[���u���b�N��
			for(int i=0;i<simSize-1;i++){
				for(int j=0;j<simSize-1;j++){
					if(b[i][j]==b[i+1][j] && b[i][j]==b[i][j+1] && b[i][j]==b[i+1][j+1]){
							//System.out.println("���F�̃��W���[���u���b�N s["+j+"]["+i+"]-s["+(j+1)+"]["+(i+1)+"]�F-3");
							score+=3;
					}
				}
			}
		//�S�̂ɐ�߂�Õ����̊���
			count=0;
			for(int i=0;i<simSize;i++){
				for(int j=0;j<simSize;j++){
					if(b[i][j]){
						count++;
					}
				}
			}
			//�f�o�b�O�p
				//System.out.println("�S��="+(simSize*simSize)+"�@��="+count);
			float hi,hi2;
			hi=(float)count/(simSize*simSize)*100;
			hi2=Math.abs(hi-50);
			//System.out.println("�S�̂ɐ�߂�Õ����̊���="+(int)hi+"%�F-"+((int)(hi2/5)*10+10));
			score+=(int)(hi2/5)*10+10;
		//�s�E��ɂ�����1:1:3:1:1�䗦�̃p�^�[��
		int hi3=0;
		int count=0;
		int dankai=0;
		int hit;
		int Save=0;
		int nowX=0,nowY=0;
		//��
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
					//System.out.println("�i�K1�@�䗦="+hi3+"�@s["+nowY+"]["+nowX+"]");
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
					//System.out.println("�i�K2�@�䗦="+hi3+"�@s["+nowY+"]["+nowX+"]");
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
					//System.out.println("�i�K3�@�䗦="+hi3+"�@s["+nowY+"]["+nowX+"]");
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
					//System.out.println("�i�K4�@�䗦="+hi3+"�@s["+nowY+"]["+nowX+"]");
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
					//System.out.println("�i�K5�@�䗦="+hi3+"�@s["+nowY+"]["+nowX+"]");
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
						//System.out.println("1:1:3:1:1�̔䗦�@s["+nowY+"]["+nowX+"]�F-30");
						score+=30;
						hit=0;
						dankai=0;
						hi3=0;
					}
				}
			}
		}
		//�s
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
					//System.out.println("�i�K1�@�䗦="+hi3+"�@s["+nowY+"]["+nowX+"]");
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
					//System.out.println("�i�K2�@�䗦="+hi3+"�@s["+nowY+"]["+nowX+"]");
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
					//System.out.println("�i�K3�@�䗦="+hi3+"�@s["+nowY+"]["+nowX+"]");
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
					//System.out.println("�i�K4�@�䗦="+hi3+"�@s["+nowY+"]["+nowX+"]");
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
					//System.out.println("�i�K5�@�䗦="+hi3+"�@s["+nowY+"]["+nowX+"]");
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
						//System.out.println("1:1:3:1:1�̔䗦�@s["+nowY+"]["+nowX+"]�F-30");
						score+=30;
						hit=0;
						dankai=0;
						hi3=0;
					}
				}
			}
		}




	System.out.println("���_="+score);
	return score;
	}

	///////////////////////////////////////////////////////////////////
	////////////�V���{����int����boolean�ɕϊ����郁�\�b�h/////////////
	public boolean[][] simbolToBoolean(int[][] a){
		boolean[][] b=new boolean[simSize][simSize];
		for(int i=0;i<simSize;i++){
			for(int j=0;j<simSize;j++){
				switch(a[i][j]){
					case NO_DATA:
						//�f�[�^����`
						b[i][j]=false;
						break;
					case BLACK_DATA:
						//���f�[�^
						b[i][j]=true;
						break;
					case WHITE_DATA:
						//���f�[�^
						b[i][j]=false;
						break;
					case K_DATA:
						//�`���A�^�ԏ�񂪓���\��
						b[i][j]=false;
						break;
					case DATA_1:
						//�f�[�^1
						b[i][j]=true;
						break;
					case DATA_0:
						//�f�[�^0
						b[i][j]=false;
						break;
					case DATA_VER:
						//�^�ԏ��
						b[i][j]=true;
						break;
					case DATA_K:
						//�`�����
						b[i][j]=true;
						break;
					default:
						//�G���[
						System.out.println("�G���[�F�@�V���{���̃f�[�^�����������ł��B�@�F"+a[i][j]);
						info.setText(info.getText()+"�G���[�F�@�V���{���̃f�[�^�����������ł��B�@�F"+a[i][j]+BR);
				}
			}
		}
		return b;
	}

	///////////////////////////////////////////////////////////////////
	////////////////////��ʂ̏����������郁�\�b�h/////////////////////
	static void init(){
		//�ϐ�������
			ver=0;
			lv=0;
			encodeMode=0;
			mask=0;
		//���̓t�B�[���h������
			inputD.setText("");
		//�e�R���{�{�b�N�X������
			verList.setSelectedIndex(ver);
			lvList.setSelectedIndex(lv);
			maskList.setSelectedIndex(mask);
	}
}
/////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////QR�R�[�h�̕\���N���X////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////

class QRShow extends JFrame{
	ImageIcon icon = new ImageIcon("icon.png");						//�A�C�R���̐ݒ�
	public static GPanel[] Grap=new GPanel[QRCode.simbolNumMax];	//�O���t�B�b�N�p�l���̌^
	public static int blockWidth= 4;									//�u���b�N�̑傫�����i�[
	public static int quiet=blockWidth*5;							//�N���C�G�b�g�]�[���̑傫�����i�[
	public int windowSizeX;											//�E�C���h�E�̑傫�����i�[
	public int windowSizeY;											//�E�C���h�E�̑傫�����i�[
	BufferedImage readImage=null;
	int imageX=0;
	int imageY=0;
	QRShow(int simbolNum,int[][] simbol,int simSize,BufferedImage readImage,int imageX,int imageY){
		//�O���t�B�b�N�p�l���C���X�^���X����
			Grap[simbolNum]=new GPanel(simSize,simbol,readImage,imageX,imageY);
			//�T�C�Y����
				windowSizeX=simSize*blockWidth+(quiet*2);
				windowSizeY=windowSizeX;
				Grap[simbolNum].setPreferredSize(new Dimension(windowSizeX, windowSizeY));
		//�\��
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
		//QR�R�[�h�`��
                drawDot(g);
                        
		//�ǂݍ��݉摜�\��
		drawImage(g);
            }
            else{
                //�ǂݍ��݉摜�\��
		drawImage(g);
                //QR�R�[�h�`��
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
