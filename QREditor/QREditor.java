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
		public static final int DATA_E1 = DataBase.DATA_E1;					//�萔(�G�f�B�^�ł̉��R�[�h�f�[�^1)
		public static final int DATA_E0 = DataBase.DATA_E0;					//�萔(�G�f�B�^�ł̉��R�[�h�f�[�^0)
		public static final Color TITLE_GRAY = DataBase.TITLE_GRAY;			//�萔(�^�C�g����ʔw�i�F)
		public static final Color TITLE_GRAY2 = DataBase.TITLE_GRAY2;		//�萔(�^�C�g����ʔw�i�F2)
		public static final String BR = DataBase.BR;						//�萔(���s�R�[�h)
		public static final String GRAP_SEPARATOR=DataBase.GRAP_SEPARATOR;	//�萔(�O���t�B�b�N���̕��������i�[)
		public static final String BR_IO=DataBase.BR_IO;					//�萔(�t�@�C�����o�͎��̉��s�R�[�h�̒u����������)
		int[][][] versionMaxLen=DataBase.getVersionMaxLen();				//QR�R�[�h�d�lDB
	//GUI�I�u�W�F�N�g�錾
		JComboBox verList;													//�^�ԃ��X�g
		JComboBox lvList;													//���������X�g
		JComboBox maskList;													//�}�X�N���X�g
		//
                JComboBox imgTranList;
                JComboBox regList;
                JComboBox colorTranList;
                //
                JButton openB;														//�t�@�C���I�[�v���{�^��
		JButton saveB;														//�t�@�C���Z�[�u�{�^��
		JButton resetB;														//���Z�b�g�{�^��
		JButton colorB;													//�����[���{�^��
		JButton okB;														//OK�{�^��
		JButton ok_imgB;													//�摜��������{�^��
		JButton imageInB;													//�摜In�{�^��
		JButton addB;														//�摜���ߍ��݃{�^��
		//
                JButton sizeUp;
                JButton sizeDown;
                JButton fit;
                JButton swap;
                //
                JTextArea dataInputArea;											//�f�[�^����͂���G���A
		JScrollPane dataInputAreaS;											//���̃X�N���[����
		static JRadioButton RadioB_pencil;									//�`�惂�[�h�̃��W�I�{�^��(���M)
		static JRadioButton RadioB_eraser;									//�`�惂�[�h�̃��W�I�{�^��(�����S��)
		static JRadioButton RadioB_both;									//�`�惂�[�h�̃��W�I�{�^��(���M&�����S��)
		static JRadioButton RadioB_fill;									//�`�惂�[�h�̃��W�I�{�^��(�h��Ԃ�)
		ButtonGroup RadioBgr = new ButtonGroup();							//�`�惂�[�h�̃��W�I�{�^���̃O���[�v
		public static EGPanel EGrap;										//�O���t�B�b�N�I�u�W�F�N�g
		JScrollPane EGrapS;													//���̃X�N���[����
		BufferedImage readImageBuf = null;
		static BufferedImage readImage;
                ColorValueSliderControl tc;
		ImageIcon icon = new ImageIcon("icon.png");							//�A�C�R���̐ݒ�
	//�t�@�C���̓��o��
		JFileChooser chooser = new JFileChooser();							//�t�@�C���̓��o��
	//���j���[�o�[
		MenuItem NewFile	= new MenuItem( "�V�K�쐬" );					//[�t�@�C��]->[�V�K�쐬]
		MenuItem Open		= new MenuItem( "�J��" );						//[�t�@�C��]->[�J��]
		MenuItem Save		= new MenuItem( "�ۑ�" );						//[�t�@�C��]->[�ۑ�]
		MenuItem bOpen		= new MenuItem( "�摜�t�@�C���ǂݍ���" );		//[�t�@�C��]->[�摜�t�@�C���ǂݍ���]
		MenuItem Exit		= new MenuItem( "�G�f�B�^�����" );			//[�t�@�C��]->[�G�f�B�^�����]
		MenuItem Do			= new MenuItem( "�f�[�^��QR-Code Maker�ɑ���" );//[�ҏW]->[�f�[�^��QR-Code Maker�ɑ���]
		MenuItem ClearInput	= new MenuItem( "���̓G���A�̃N���A" );			//[�\��]->[���̓G���A�̃N���A]
		MenuItem Version	= new MenuItem( "�o�[�W�������" );				//[�w���v]->[�o�[�W�������]
                
        //���̑�
		int WindowSizeX=0;													//�E�C���h�E��X
		int WindowSizeY=0;													//�E�C���h�E��Y
		int encodeMode=0;													//�G���R�[�h���[�h
		int ver=1;															//�^�Ԃ��i�[
		int lv=0;															//�������x��
		int mask=0;															//�}�X�N
		public static float imgTran = 1.0f;
                public static float colorTran = 1.0f;
                int RSSum=1;														//RS�u���b�N��
		int mode_mozisuLen;													//���[�h�w���q�ƕ������w���q�̒���
		int verMax=40;														//�����ł���ō��̌^��
		int moveSpeed=EGPanel.blockSize;													//�摜�t�@�C���ړ��X�s�[�h	
		String notGrapCode=null;											//�O���t�B�b�N�ȊO�̕����̃r�b�g��
		String Inputdata="";												//���͂��ꂽ�f�[�^
		String dataB;														//�f�[�^�r�b�g��
		String Title="QR-Code Editor";										//�v���O�����̃^�C�g��
		String outstr=null;													//�o�͂���镶����
		static int paintMode=0;												//�`�惂�[�h(1:���M 2:�����S�� 3:���� 4:�h��Ԃ�)
		//
                static boolean fitMode = false;
                static boolean swapMode = false;
                static boolean edit = false;
                //
                static int simSize=21;												//�V���{���̃T�C�Y
		static int[][] simbol;												//�V���{�����i�[
		public static Color[][] simbolC;
                public static float[][] simbolT;
                int [] dataBit;	
                
	////////////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////�R���X�g���N�^////////////////////////////////////////
	QREditor(){
		//������
			ver=1;
			lv=0;
			mask=0;
			encodeMode=0;
			simSize=21+4*(ver-1);
			simbol=new int[simSize][simSize];
                        simbolC=new Color[simSize][simSize];
                        simbolT=new float[simSize][simSize];
                //���j���[
			MenuBar MyMenu = new MenuBar();
			Menu FileMenu = new Menu( "�t�@�C��" );
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
			Menu EditMenu = new Menu( "�ҏW" );
  			  	Do.addActionListener(this);
				EditMenu.add(Do);
			Menu ShowMenu = new Menu( "�\��" );
  			  	ClearInput.addActionListener(this);
				ShowMenu.add(ClearInput);
			Menu HelpMenu = new Menu( "�w���v" );
  			  	Version.addActionListener(this);
				HelpMenu.add(Version);
			MyMenu.add( FileMenu );
			MyMenu.add( EditMenu );
			MyMenu.add( ShowMenu );
			MyMenu.add( HelpMenu );
			setMenuBar(MyMenu);
		//�E�C���h�E�T�C�Y����
			windowResize(simSize);
		//�O���t�B�b�N�G���A
			EGrap=new EGPanel();
			EGrap.addMouseListener(new MyMouseListener());
			EGrap.addMouseMotionListener(new MyMouseMotionListener());
			EGrap.addKeyListener(this);
		//�{�^��
			//�t�@�C���I�[�v���{�^��
				openB=new JButton("�J��");
				openB.addActionListener(this);
				openB.addKeyListener(this);
			//�t�@�C���Z�[�u�{�^��
				saveB=new JButton("�ۑ�");
				saveB.addActionListener(this);
				saveB.addKeyListener(this);
			//���Z�b�g�{�^��
				resetB=new JButton("�N���A");
				resetB.addActionListener(this);
				resetB.addKeyListener(this);
			//�J���[���{�^��
				colorB=new JButton("�J���[��");
				colorB.addActionListener(this);
				colorB.addKeyListener(this);
			//OK�{�^��
				okB=new JButton("OK");
				okB.addActionListener(this);
				okB.addKeyListener(this);
			//�摜��������{�^��
				ok_imgB=new JButton("�摜��������");
				ok_imgB.addActionListener(this);
				ok_imgB.addKeyListener(this);
			//�摜�ǂݍ��݃{�^��
				imageInB=new JButton("��");
				imageInB.addActionListener(this);
				imageInB.addKeyListener(this);
			//�摜���ߍ��݃{�^��
				addB=new JButton("��");
				addB.addActionListener(this);
				addB.addKeyListener(this);
                        //�摜��傫������{�^��
                                sizeUp = new JButton("��");
                                sizeUp.addActionListener(this);
				sizeUp.addKeyListener(this);
                        //�摜������������{�^��
                                sizeDown = new JButton("��");
                                sizeDown.addActionListener(this);
				sizeDown.addKeyListener(this);
                        // 
                                fit = new JButton("�L"); 
                                fit.addActionListener(this);
				fit.addKeyListener(this);
                        //
                                swap = new JButton("��"); 
                                swap.addActionListener(this);
				swap.addKeyListener(this);
		//�f�[�^���̓G���A
			dataInputArea=new JTextArea("", 4,38);
			dataInputArea.setLineWrap(true);
			dataInputAreaS=new JScrollPane(dataInputArea);
			dataInputAreaS.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			dataInputAreaS.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			dataInputAreaS.setPreferredSize(new Dimension(WindowSizeX-25, 70));
			dataInputAreaS.addKeyListener(this);
		//�O���t�B�b�N�I�u�W�F�N�g�̃X�N���[���ݒ�
			EGrapS=new JScrollPane(EGrap);
			EGrapS.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			EGrapS.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
			EGrapS.setPreferredSize(new Dimension(500, 420));
			EGrapS.addKeyListener(this);
		//�R���{�{�b�N�X
			//�������x��
				lvList = new JComboBox();
				lvList.addItem(" L ( 7%)");
				lvList.addItem(" M (15%)");
				lvList.addItem(" Q (25%)");
				lvList.addItem(" H (30%)");
				lvList.setLightWeightPopupEnabled(false);	//�d�ʃR���|�[�l���g�����ăv���_�E�����\������Ȃ��s�����
				lvList.addActionListener(this);
				lvList.addKeyListener(this);
			//�^��
				verList = new JComboBox();
				for(int i=1;i<=verMax;i++){
					verList.addItem(Integer.toString(i)+" ");
				}
				verList.setLightWeightPopupEnabled(false);	//�d�ʃR���|�[�l���g�����ăv���_�E�����\������Ȃ��s�����
				verList.addActionListener(this);
				verList.addKeyListener(this);
			//�}�X�N
				maskList = new JComboBox();
				for(int i=0;i<8;i++){
					maskList.addItem(Integer.toString(i)+"  ");
				}
				maskList.setLightWeightPopupEnabled(false);	//�d�ʃR���|�[�l���g�����ăv���_�E�����\������Ȃ��s�����
				maskList.addActionListener(this);
				maskList.addKeyListener(this);
                        //�C���[�W����
                                imgTranList = new JComboBox();
                                for(int i=100;i>=0;i--){
					imgTranList.addItem(Integer.toString(i)+" %");
				}
                                imgTranList.setSelectedIndex(100);
                                imgTranList.setLightWeightPopupEnabled(false);	//�d�ʃR���|�[�l���g�����ăv���_�E�����\������Ȃ��s�����
				imgTranList.addActionListener(this);
				imgTranList.addKeyListener(this);
                        //���ÔF��
                                regList = new JComboBox();
                                for(int i=0;i<=100;i++){
					regList.addItem(Integer.toString(i)+" %");
				}
                                regList.setSelectedIndex(80);
                                regList.setLightWeightPopupEnabled(false);	//�d�ʃR���|�[�l���g�����ăv���_�E�����\������Ȃ��s�����
				regList.addActionListener(this);
				regList.addKeyListener(this);
                        //�h�b�g����
                                colorTranList = new JComboBox();
                                for(int i=100;i>=0;i--){
					colorTranList.addItem(Integer.toString(i)+" %");
				}
                                colorTranList.setSelectedIndex(100);
                                colorTranList.setLightWeightPopupEnabled(false);	//�d�ʃR���|�[�l���g�����ăv���_�E�����\������Ȃ��s�����
				colorTranList.addActionListener(this);
				colorTranList.addKeyListener(this);
		//���W�I�{�^��
			paintMode=0;
			RadioB_pencil=new JRadioButton("���M",true);
			RadioB_eraser=new JRadioButton("�����S��");
			RadioB_both=new JRadioButton("�̈�&�w�i");
			RadioB_fill=new JRadioButton("�h��Ԃ�");
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
		//���C�A�E�g
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
			p1.add(new JLabel("QR�R�[�h������f�[�^"));
			p2.setLayout(new FlowLayout());
			p2.addKeyListener(this);
			p2.add(dataInputAreaS);
			p3.addKeyListener(this);
			p3.add(new JLabel("����"));
			p3.add(lvList);
			p3.add(new JLabel("�^��"));
			p3.add(verList);
			p3.add(new JLabel("�}�X�N"));
			p3.add(maskList);
			p3.add(imageInB);
			p3.add(addB);
			p3.add(colorB);
			p4.addKeyListener(this);
			p4.add(new JLabel("�`�惂�[�h�F"));
			p4.add(RadioB_pencil);
			p4.add(RadioB_eraser);
			p4.add(RadioB_both);
			p4.add(RadioB_fill);
                        p4.add(new JLabel("�P�x�l"));
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
                        p7.add(new JLabel("�摜�ҏW"));
                        p7.add(sizeUp);
                        p7.add(sizeDown);
                        p7.add(fit);
                        p7.add(swap);
                        p7.add(new JLabel("����"));
                        p7.add(imgTranList);
                        p7.add(colorTranList);
                        //
			setIconImage(icon.getImage());
		//�\��
			init();
			setTitle(Title);
			setVisible(true);
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////�C�x���g���\�b�h////////////////////////////////////////
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		//�{�^��(�J��)�̃C�x���g
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
									if(ioData2.length==simSizeTmp){
										for(int j=0;j<simSizeTmp;j++){
											simbolTmp[i][j]=Integer.parseInt(ioData2[j]);
										}
									}else{
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
		//�{�^��(�ۑ�)�̃C�x���g
			if(obj==saveB){
				data_out();
				int returnVal = chooser.showSaveDialog(this);
				try{
					if (returnVal == JFileChooser.APPROVE_OPTION) {
						BufferedWriter out = new BufferedWriter(new FileWriter(chooser.getSelectedFile()));
						//�^�ԁA���[�h�A�������x���A�}�X�N�����o��
							String[] ioData=new String[1];
							ioData[0]=ver+","+encodeMode+","+lv+","+mask;
							out.write(ioData[0]);
							out.newLine();
						//�V���{�������o��
							for(int i=0;i<simSize;i++){
								ioData[0]="";
								for(int j=0;j<simSize;j++){
									ioData[0]+=simbol[i][j]+",";
								}
								out.write(ioData[0]);
								out.newLine();
							}
						//���̓f�[�^�����o��
							out.write(Inputdata);
							out.newLine();
						//�o�̓f�[�^�����o��
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
		//�{�^��(�J���[��)�̃C�x���g
			if(obj==colorB){
				tc = new ColorValueSliderControl();
                                tc.setLocation(0, 0);
			}
		//�{�^��(���Z�b�g)�̃C�x���g
			if(obj==resetB){
				init();
			
			}
		//�{�^��(OK)�̃C�x���g
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
		//�{�^��(�摜��������)�̃C�x���g
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
		//�{�^��(�摜In)�̃C�x���g
			if(obj==imageInB){
				Image readImageIm;
				int returnVal = chooser.showOpenDialog(this);
				try {
					if (returnVal == JFileChooser.APPROVE_OPTION) {
						File file = chooser.getSelectedFile();
						try {
 							readImageBuf = ImageIO.read(file);
                                                        QRCode.imageSize = 0;
							//�摜�T�C�Y�̒���
								//readImageIm=readImageBuf.getScaledInstance((int)(readImageBuf.getWidth()*((float)EGPanel.blockSize/QRShow.blockWidth)), -1, Image.SCALE_AREA_AVERAGING);
                                                                readImageIm=readImageBuf.getScaledInstance((int)(QRCode.initImageSize*((float)EGPanel.blockSize))-1, -1, Image.SCALE_AREA_AVERAGING);
							//Image��BufferedImage�ɕϊ�
								readImage=toBufferedImage(readImageIm);
						} catch (Exception e2) {
							e2.printStackTrace();
							readImageBuf = null;
						}
					}
				} catch(Exception ex){}



				update();
			}
		//�{�^��(�摜���ߍ���)�̃C�x���g
			if(obj==addB && readImage!=null){
				int sp_x=0;
				int sp_y=0;
				int ep_x=0;
				int ep_y=0;
				int x=0;
				int y=0;
				int rgb=0;
				//���F����
					readImage=imgToGray(readImage);
				//���C������
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
								//�f�o�b�O�p
								System.out.println("���W���摜�̃s�N�Z���O x="+x+" y="+y);
							}
							if(rgb!=-1){
								EGPanel.bitTurn(i,j);
							}
						}
					}
				update();
			}
		//�R���{�{�b�N�X(�^��)�̃C�x���g
			if(obj==verList){
				ver=verList.getSelectedIndex()+1;
				init();
			}
		//�R���{�{�b�N�X(�������x��)�̃C�x���g
			if(obj==lvList){
				lv=lvList.getSelectedIndex();
				init();
			}
		//�R���{�{�b�N�X(�}�X�N)�̃C�x���g
			if(obj==maskList){
				mask=maskList.getSelectedIndex();
				init();
			}
                //�C���[�W����
                        if(obj==imgTranList){
				imgTran = (float)imgTranList.getSelectedIndex()/100;
				update();
			}
                //���ÔF��
                        if(obj==regList){
				EGPanel.regList = regList.getSelectedIndex();
                                //JOptionPane.showMessageDialog(null,regList.getSelectedIndex());
                                JOptionPane.showMessageDialog(null, "���ËP�x�l��"+regList.getSelectedIndex()
                                            + "�ɐݒ肳��܂����B\n"
                                            +"�P�x�l��ݒ肷�邱�Ƃ͈��̂�\n"
                                            + "�ύX�������ꍇ�́w�N���A�x�{�^���őS�Ă����Z�b�g���Ă��������B");
                                regList.setEnabled(false);
                                edit = true;
				update();
			}
                //�h�b�g����
                        if(obj==colorTranList){
				colorTran = (float)colorTranList.getSelectedIndex()/100;
				update();
			}
		//���W�I�{�^��(���M)�̃C�x���g
			if(obj==RadioB_pencil){
				paintMode=0;
			}
		//���W�I�{�^��(�����S��)�̃C�x���g
			if(obj==RadioB_eraser){
				paintMode=1;
			}
		//���W�I�{�^��(���M&�����S��)�̃C�x���g
			if(obj==RadioB_both){
				paintMode=2;
			}
		//���W�I�{�^��(�h��Ԃ�)�̃C�x���g
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
	///////////////////////////////////////////���\�b�h//////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////


	//////////////////////////////////////////////////////////////
	//////////////////�V���{�������������郁�\�b�h////////////////
	public void init (){
		simSize = 21+(4*(ver-1));
		simbol = new int[simSize][simSize];
		simbolC = new Color[simSize][simSize];
                simbolT = new float[simSize][simSize];
                setSimbolInitData();
		readImage=null;
		EGPanel.imageX=0;
		EGPanel.imageY=0;
		//��ʍX�V
                regList.setEnabled(true);
                edit = false;
		update();
	}

	//////////////////////////////////////////////////////////////
	///////////////////��ʂ��X�V���郁�\�b�h/////////////////////
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
	////////////�N���b�N�h���b�O���Ɏ��s����郁�\�b�h////////////
	public static void Clicked(int x,int y) {
		EGrap.Clicked(x,y,paintMode);
	}

	//////////////////////////////////////////////////////////////
	/////////�h���b�O���I���������W�����Z�b�g���郁�\�b�h///////
	public static void Released() {
		EGrap.Released();
	}

	//////////////////////////////////////////////////////////////
	////////////�E�C���h�E�T�C�Y��K���ɂ��郁�\�b�h//////////////
	public void windowResize(int simSize){
		WindowSizeX=650;
		WindowSizeY=670;
		setSize(WindowSizeX,WindowSizeY);
	}

	//////////////////////////////////////////////////////////////////////
	/////////////////////////////�o�̓��\�b�h/////////////////////////////
	public void data_out(){
		RSSum=versionMaxLen[ver][lv][6]+versionMaxLen[ver][lv][7];
		dataB="";
		String[] dataB8; 
		//�V���{���̃R�s�[simbol_copy���擾
			int[][] simbol_copy=new int[simSize][];
			for(int i=0;i<simSize;i++){
				simbol_copy[i]=(int[])simbol[i].clone();
			}
		//�f�[�^�����Ƀ}�X�N��������
			DataBase.mask(simbol_copy,mask,simSize,2);
		//�O���t�B�b�N�����̃r�b�g����擾
			//�V���{���̑S�r�b�g�񒊏o
					dataB=getBit(simSize,simbol_copy,0);
			//RSB�������̎��̓C���^���[�u�擾
				if(RSSum>1){
					String[][] RSB=new String[RSSum][];
					int dBkazu=(int)(versionMaxLen[ver][lv][4]/RSSum);
					for(int i=0;i<versionMaxLen[ver][lv][6];i++){
						RSB[i]=new String[dBkazu];
					}
					for(int i=versionMaxLen[ver][lv][6];i<RSSum;i++){
						RSB[i]=new String[dBkazu+1];
					}
					//�f�[�^�r�b�g���8�r�b�g���ɋ�؂�
						dataB8=new String[dataB.length()/8];
						for(int i=0;i<dataB8.length;i++){
							dataB8[i]=dataB.substring((i*8),(i*8)+8);
						}
					//RSB�z��ɏ��ԂɃf�[�^�r�b�g����i�[���Ă���
						int nowRSB=0;
						for(int i=0;i<dataB8.length;i++){
							if(i==RSSum*dBkazu){
								nowRSB=versionMaxLen[ver][lv][6];
							}
							RSB[nowRSB][(int)i/RSSum]=dataB8[i];
							nowRSB++;
							nowRSB%=RSSum;
						}
					//RSB�z����P�ɂ܂Ƃ߂�
						dataB="";
						for(int i=0;i<RSSum;i++){
							for(int j=0;j<RSB[i].length;j++){
								dataB+=RSB[i][j];
							}
						}
				}
		//�S�r�b�g�񂩂�O���t�B�b�N�����̃r�b�g��݂̂��擾
			dataB=dataB.substring(mode_mozisuLen);
		//�f�[�^�̏o��
			outstr=Inputdata+GRAP_SEPARATOR+dataB;
	}

	//////////////////////////////////////////////////////////////////////
	///////////////�V���{���ɏ����f�[�^�r�b�g��ݒ肷�郁�\�b�h///////////
	public void setSimbolInitData(){
		//���̓f�[�^�擾
			Inputdata=dataInputArea.getText();
			//���̓f�[�^���e�ʂ𒴂��Ă�������̓f�[�^����
				//�쐬��
			encodeMode=DataBase.getEncodeMode(Inputdata);
			String InputdataBit=DataBase.DtoB(Inputdata,encodeMode);
		//�O���t�B�b�N�r�b�g�O�̑S�r�b�g�����v�Z
			mode_mozisuLen=4+DataBase.mozisusizisiLenGet(ver,encodeMode);
			mode_mozisuLen+=InputdataBit.length();
			//�I�[�p�^�[���̐����v�Z
				String terminator="";
				int terminatorLen=((versionMaxLen[ver][lv][4]*8)-mode_mozisuLen);
				terminatorLen=terminatorLen>4?4:terminatorLen;
				//�f�o�b�O�p
					System.out.println("�I�[�p�^�[����="+terminatorLen);
				for(int i=0;i<terminatorLen;i++){
					terminator+="0";
					mode_mozisuLen++;
				}
		//�O���t�B�b�N�r�b�g�O�̑S�r�b�g�f�[�^���擾
			notGrapCode=DataBase.getMode(encodeMode);														//�f�[�^���[�h�w���q
			notGrapCode+=DataBase.get2(Inputdata.length(),DataBase.mozisusizisiLenGet(ver,encodeMode));		//�f�[�^�������w���q
			notGrapCode+=InputdataBit;																		//�f�[�^
			notGrapCode+=terminator;																		//�I�[�p�^�[��
			//�O���t�B�b�N�����O�̑S�r�b�g�f�[�^���A�V���{���Ɋi�[����`�ɕϊ�
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
		//�C���^���[�u�z�u
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
				//�P�߂�RS�u���b�N��
					int tmp=0;
					for(int i=0;i<versionMaxLen[ver][lv][6];i++){
						System.out.println("1�߂�RS�u���b�N");
						dBkazu=(int)(versionMaxLen[ver][lv][4]/RSSum);
						RSB[i]=new String[dBkazu];
						for(int j=0;j<dBkazu;j++){
							RSB[i][j]=dataB8[tmp++];
						}
					}
				//2�߂�RS�u���b�N��
					for(int i=0;i<versionMaxLen[ver][lv][7];i++){
						System.out.println("2�߂�RS�u���b�N");
						dBkazu=(int)(versionMaxLen[ver][lv][4]/RSSum)+1;
						RSB[i+versionMaxLen[ver][lv][6]]=new String[dBkazu];
						for(int j=0;j<dBkazu;j++){
							RSB[i+versionMaxLen[ver][lv][6]][j]=dataB8[tmp++];
						}
					}
				//�f�[�^��z�u���鏇�Ƀ\�[�g
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
		//�V���{���ɌŒ�f�[�^���ߍ���
			DataBase.Indata(ver,simSize,simbol);
		//�r�b�g����V���{���ɖ��ߍ���
			setBit(simSize,dataBit,simbol);
		//E1,E0�̂݃}�X�N��K�p
			DataBase.mask(simbol,mask,simSize,1);
		//�`�����𖄂ߍ���
			DataBase.setFormatInformation(simbol,lv,mask);
	}

	//////////////////////////////////////////////////////////////
	////////�V���{���Ƀf�[�^�r�b�g���}�����郁�\�b�h////////////
	public void setBit(int simSize,int[] haitiBit,int[][] simbol){
		//�����ݒ�
			int direct=1;
		  	int LeftOrRight=1;
		  	int nowX=simSize-1;
		  	int nowY=simSize-1;
			int haitiBitLength;
			int[] tmp=new int[2];
		//�z�u
			haitiBitLength=haitiBit.length;
			tmp[1]=0;
			for(int i=0;tmp[1]<haitiBit.length;i++){
				tmp[0]=0;
				//�f�o�b�O�p
					if(nowX<-1){
						System.out.println("�G���[�F�@�f�[�^���V���{���Ɏ��܂肫��܂���@�@�c��r�b�g���F"+(haitiBit.length-i-1));
						QRCode.info.setText(QRCode.info.getText()+"�G���[�F�@�f�[�^���V���{���Ɏ��܂肫��܂���@�@�c��r�b�g���F"+(haitiBit.length-i-1)+BR);
					}
				if(nowX>=0 && nowX<=simSize-1 && nowY>=0 && nowY<=simSize-1){
					//�f�[�^�����ꂼ��z�u
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
					//�ړ�
						if(LeftOrRight==1){
							//�E��
							nowX--;
							LeftOrRight=-LeftOrRight;
						}else if(nowY>=0 && nowY<=simSize-1){
							//�㉺�ړ��\
							nowY-=direct;
							nowX++;
							LeftOrRight=-LeftOrRight;
						}else{
							//�㉺�ړ��s�\
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
	////////�V���{������f�[�^�r�b�g������o�����\�b�h//////////
	public String getBit(int simSize,int[][] simbol,int mode){
		//
		//mode=1�F�O���t�B�b�N�����̃r�b�g���o
		//mode=2�F�O���t�B�b�N�ȊO�̕����̃r�b�g���o
		//mode=0�F�������o
		//
		//�����ݒ�
			int direct=1;
		  	int LeftOrRight=1;
		  	int nowX=simSize-1;
		  	int nowY=simSize-1;
			int[] tmp=new int[2];
			String dataB="";
		//�z�u
			tmp[1]=0;
			for(int i=0;tmp[1]<versionMaxLen[ver][lv][4]*8;i++){
				tmp[0]=0;
				//�f�o�b�O�p
					if(nowX<-1){
						//�G���[
					}
				if(nowX>=0 && nowX<=simSize-1 && nowY>=0 && nowY<=simSize-1){
					//�f�[�^�����o��
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
					//�ړ�
						if(LeftOrRight==1){
							//�E��
							nowX--;
							LeftOrRight=-LeftOrRight;
						}else if(nowY>=0 && nowY<=simSize-1){
							//�㉺�ړ��\
							nowY-=direct;
							nowX++;
							LeftOrRight=-LeftOrRight;
						}else{
							//�㉺�ړ��s�\
							nowX--;
							if(nowX==6){
								nowX--;
							}
							LeftOrRight=-LeftOrRight;
							nowY+=direct;
							direct=-direct;	
						}
						if(nowX<-1){
							System.out.println("�G���[�FgetBit()�ɂăV���{�����擾������܂���ł����B");
							QRCode.info.setText(QRCode.info.getText()+"�G���[�FgetBit()�ɂăV���{�����擾������܂���ł����B"+BR);
							break; 
						}
			 	}
			}
		return dataB;
	}

	//////////////////////////////////////////////////////////////
	////////////Image��BufferedImage�ɕϊ����郁�\�b�h////////////
	static public BufferedImage toBufferedImage(Image img){
		BufferedImage bimg=null;
		try{
			//java.awt.MediaTracker �Ń��[�h��ҋ@
				MediaTracker tracker = new MediaTracker(new Component(){});
				tracker.addImage(img, 0);
				tracker.waitForAll();
      		//Image��BufferedImage�ɕϊ�
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
	/////////�摜���O���[�X�P�[���ɕϊ����郁�\�b�h///////////////
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
	/////////��f���O���[�X�P�[���ɕϊ����郁�\�b�h///////////////
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
	//////////////�L�[�{�[�h���̓C�x���g���\�b�h//////////////////
	public void keyPressed(KeyEvent e) {
            //�摜�̈ړ�����
		System.out.println("�����ꂽ�L�[�R�[�h:"+e.getKeyCode());
		if(e.getKeyCode()==37){//��
			EGPanel.imageX-=moveSpeed;
		}else if(e.getKeyCode()==39){//��
			EGPanel.imageX+=moveSpeed;
		}else if(e.getKeyCode()==38){//��
			EGPanel.imageY-=moveSpeed;
		}else if(e.getKeyCode()==40){//��
			EGPanel.imageY+=moveSpeed;
		}else if(e.getKeyCode()==67){//C�@�摜�̕\���ؑ�
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
                JOptionPane.showMessageDialog(null,"�P�x�l��ݒ肵�Ă�������");
	}
}

class MyMouseMotionListener implements MouseMotionListener{
	public void mouseDragged( MouseEvent e ) {
            if(QREditor.edit)
		QREditor.Clicked(e.getX(),e.getY());
            else
                JOptionPane.showMessageDialog(null,"�P�x�l��ݒ肵�Ă�������");
	}
	public void mouseMoved( MouseEvent e ) {
            if(QREditor.edit)
                QREditor.Released();
	}

}

/////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////�O���t�B�b�N�p�l��////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////
class EGPanel extends JPanel{
	//DataBase����f�[�^�擾
		public static final int NO_DATA = DataBase.NO_DATA;					//�萔(�f�[�^��)
		public static final int BLACK_DATA = DataBase.BLACK_DATA;			//�萔(���f�[�^)
		public static final int WHITE_DATA = DataBase.WHITE_DATA;			//�萔(���f�[�^)
		public static final int K_DATA = DataBase.K_DATA;					//�萔(�`���A�^�ԗ\��)
		public static final int DATA_1 = DataBase.DATA_1;					//�萔(�R�[�h�f�[�^1)
		public static final int DATA_0 = DataBase.DATA_0;					//�萔(�R�[�h�f�[�^0)
		public static final int DATA_VER = DataBase.DATA_VER;				//�萔(�^�ԏ��)
		public static final int DATA_K = DataBase.DATA_K;					//�萔(�`�����)
		public static final int DATA_E1 = DataBase.DATA_E1;					//�萔(�G�f�B�^�ł̉��R�[�h�f�[�^1)
		public static final int DATA_E0 = DataBase.DATA_E0;					//�萔(�G�f�B�^�ł̉��R�[�h�f�[�^0)
		public static final String BR = DataBase.BR;						//�萔(���s�R�[�h)
	//�h�b�g�̃J���[
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
        //���̑�
		static int quiet=1;
		static int blockWidth=9;
		static int blockSize=blockWidth+quiet;
		static int imageX=0;
		static int imageY=0;
		static boolean imgVisible=true;										//�摜�t�@�C���̕\���ؑ�
		int nowX=-1;
		int nowY=-1;
                static int regList = 80;
                //static int regWhite = 80;										//�f�[�^�r�b�g����擾
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
                    //�ǂݍ��݉摜�\��
                drawImage(g);
            }
	}
        private void drawDot(Graphics g)
        {
            //QR�R�[�h�O�g�`��
            Graphics2D g2d = (Graphics2D)g;
            g2d.setColor(Color.black);
            //g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.5f));
            for(int i=0;i<QREditor.simSize;i++){
                for(int j=0;j<QREditor.simSize;j++){
                    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.5f));
                    switch(QREditor.simbol[j][i]){
                        case NO_DATA:
                                //�f�[�^����`
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
                                //���f�[�^
                                g2d.setColor(Color.BLUE);
                                g2d.drawRect(i*blockSize,j*blockSize,blockWidth,blockWidth);
                                g2d.setColor(BLACK_DATA_COLOR);
                                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,QREditor.colorTran));
                                g2d.fillRect(i*blockSize,j*blockSize,blockWidth,blockWidth);
                                break;
                        case WHITE_DATA:
                                //���f�[�^
                                g2d.setColor(Color.BLUE);
                                g2d.drawRect(i*blockSize,j*blockSize,blockWidth,blockWidth);
                                g2d.setColor(BACKGROUND_COLOR);
                                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.3f));
                                g2d.fillRect(i*blockSize,j*blockSize,blockWidth,blockWidth);
                                break;
                        case K_DATA:
                                //�`���A�^�ԏ�񂪓���\��n
                                g2d.setColor(K_DATA_COLOR);
                                g2d.drawRect(i*blockSize,j*blockSize,blockWidth,blockWidth);
                                break;
                        case DATA_1:
                                //�f�[�^1
                                g2d.setColor(Color.BLACK);
                                g2d.drawRect(i*blockSize,j*blockSize,blockWidth,blockWidth);
                                g2d.setColor(QREditor.simbolC[j][i]);
                                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,QREditor.simbolT[j][i]));
                                g2d.fillRect(i*blockSize,j*blockSize,blockWidth,blockWidth);
                                break;
                        case DATA_0:
                                //�f�[�^0
                                g2d.setColor(Color.BLACK);
                                g2d.drawRect(i*blockSize,j*blockSize,blockWidth,blockWidth);
                                g2d.setColor(QREditor.simbolC[j][i]);
                                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,QREditor.simbolT[j][i]));
                                g2d.fillRect(i*blockSize,j*blockSize,blockWidth,blockWidth);
                                //System.out.println(QREditor.simbolC[j][i].getRed());
                                break;
                        case DATA_VER:
                                //�^�ԏ��
                                g2d.setColor(Color.GREEN);
                                g2d.drawRect(i*blockSize,j*blockSize,blockWidth,blockWidth);
                                g2d.setColor(DATA_VER_COLOR);
                                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,QREditor.colorTran));
                                g2d.fillRect(i*blockSize,j*blockSize,blockWidth,blockWidth);
                                break;
                        case DATA_K:
                                //�^�ԏ��
                                g2d.setColor(Color.GRAY);
                                g2d.drawRect(i*blockSize,j*blockSize,blockWidth,blockWidth);
                                g2d.setColor(DATA_K_COLOR);
                                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,QREditor.colorTran));
                                g2d.fillRect(i*blockSize,j*blockSize,blockWidth,blockWidth);
                                break;
                        case DATA_E1:
                                //�G�f�B�^�Œ�f�[�^1
                                QREditor.simbolC[j][i] = DATA_E_COLOR;
                                //QREditor.simbolT[j][i] = tranValue;
                                g2d.setColor(Color.MAGENTA);
                                g2d.drawRect(i*blockSize,j*blockSize,blockWidth,blockWidth);
                                g2d.setColor(DATA_E_COLOR);
                                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,QREditor.colorTran));
                                g2d.fillRect(i*blockSize,j*blockSize,blockWidth,blockWidth);
                                break;
                        case DATA_E0:
                                //�G�f�B�^�Œ�f�[�^0
                                QREditor.simbolC[j][i] = BACKGROUND_COLOR;
                                //QREditor.simbolT[j][i] = tranValue;
                                g2d.setColor(Color.MAGENTA);
                                g2d.drawRect(i*blockSize,j*blockSize,blockWidth,blockWidth);
                                g2d.setColor(BACKGROUND_COLOR);
                                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.3f));
                                g2d.fillRect(i*blockSize,j*blockSize,blockWidth,blockWidth);
                                break;
                        default:
                                //�G���[
                                System.out.println("�G���[�F�@�V���{���̃f�[�^�����������ł��B�@�F"+QREditor.simbol[i][j]);
                                QRCode.info.setText(QRCode.info.getText()+"�G���[�F�@�V���{���̃f�[�^�����������ł��B�@�F"+QREditor.simbol[i][j]+BR);
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
            //����`
        }
	public void Clicked(int x,int y,int paintMode){
		if(x>=0 && y>=0 && x<=QREditor.simSize*(blockWidth+quiet) && y<=QREditor.simSize*(blockWidth+quiet)){
			x=(int)x/(blockWidth+quiet);
			y=(int)y/(blockWidth+quiet);
			if(nowX!=x || nowY!=y){
				nowX=x;
				nowY=y;
				if((QREditor.simbol[y][x]==DATA_0||QREditor.simbol[y][x]==DATA_1) && paintMode==1){
					//�����S��&����
					QREditor.simbol[y][x]=DATA_0;
                                        QREditor.simbolC[y][x] = BACKGROUND_COLOR;
                                        QREditor.simbolT[y][x] = 0.0f;
				}else if((QREditor.simbol[y][x]==DATA_0||QREditor.simbol[y][x]==DATA_1) && paintMode==0){
					//���M&����
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
					//�h��Ԃ�
				}else if(QREditor.simbol[y][x]==NO_DATA&&briV<regList && paintMode==2){
					NO_DATA_COLOR = rgbValue;
				}
                                else if(QREditor.simbol[y][x]==BLACK_DATA&&briV<regList && paintMode==2){
					BLACK_DATA_COLOR = rgbValue;
				}
                                else if(QREditor.simbol[y][x]==K_DATA && paintMode==2){
					//����`
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
                                                //����`
				repaint();
			}
		}else{
			System.out.println("�g�O�ł�");
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