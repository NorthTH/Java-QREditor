//各データベース
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.WindowAdapter; 
import java.awt.event.WindowEvent;

class DataBase{

	//////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////定数//////////////////////////////////////////////////
	public static final int NO_DATA = 		0;								//定数(データ無)
	public static final int BLACK_DATA = 	1;								//定数(黒データ)
	public static final int WHITE_DATA = 	2;								//定数(白データ)
	public static final int K_DATA =		3;								//定数(形式、型番予定)
	public static final int DATA_1 =		4;								//定数(コードデータ1)
	public static final int DATA_0 =		5;								//定数(コードデータ0)
	public static final int DATA_VER =		6;								//定数(型番情報)
	public static final int DATA_K =		7;								//定数(形式情報)
	public static final int DATA_E1 =		8;								//定数(エディタでの仮コードデータ1)
	public static final int DATA_E0 =		9;								//定数(エディタでの仮コードデータ0)
	public static final String BR = "\n";									//定数(改行コード)
	public static final String GRAP_SEPARATOR="/Graphics/";					//定数(グラフィック部の分離符を格納)
	public static final String BR_IO="-B-";									//定数(ファイル入出力時の改行コードの置き換え文字)
	public static final Color TITLE_GRAY=new Color(226,226,226);			//定数(タイトル画面背景色)
	public static final Color TITLE_GRAY2=new Color(230,230,230);			//定数(タイトル画面背景色2)

	//////////////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////QRコード仕様の生データ//////////////////////////////////////////
	final static int[] maxLenDB={
	//		数字	英数 	8bit	漢字	コード数	訂正コード数	RS1		RS2		総コード語数
	/*1L*/	0		,0		,0		,0		,0			,7				,1		,0		,26,
	/*1M*/	0		,0		,0		,0		,0			,10				,1		,0		,26,
	/*1Q*/	0		,0		,0		,0		,0			,13				,1		,0		,26,
	/*1H*/	0		,0		,0		,0		,0			,17				,1		,0		,26,
	/*2L*/	0		,0		,0		,0		,0			,10				,1		,0		,44,
	/*2M*/	0		,0		,0		,0		,0			,16				,1		,0		,44,
	/*2Q*/	0		,0		,0		,0		,0			,22				,1		,0		,44,
	/*2H*/	0		,0		,0		,0		,0			,28				,1		,0		,44,
	/*3L*/	0		,0		,0		,0		,0			,15				,1		,0		,70,
	/*3M*/	0		,0		,0		,0		,0			,26				,1		,0		,70,
	/*3Q*/	0		,0		,0		,0		,0			,36				,2		,0		,70,
	/*3H*/	0		,0		,0		,0		,0			,44				,2		,0		,70,
	/*4L*/	0		,0		,0		,0		,0			,20				,1		,0		,100,
	/*4M*/	0		,0		,0		,0		,0			,36				,2		,0		,100,
	/*4Q*/	0		,0		,0		,0		,0			,52				,2		,0		,100,
	/*4H*/	0		,0		,0		,0		,0			,64				,4		,0		,100,
	/*5L*/	0		,0		,0		,0		,0			,26				,1		,0		,134,
	/*5M*/	0		,0		,0		,0		,0			,48				,2		,0		,134,
	/*5Q*/	0		,0		,0		,0		,0			,72				,2		,2		,134,
	/*5H*/	0		,0		,0		,0		,0			,88				,2		,2		,134,
	/*6L*/	0		,0		,0		,0		,0			,36				,2		,0		,172,
	/*6M*/	0		,0		,0		,0		,0			,64				,4		,0		,172,
	/*6Q*/	0		,0		,0		,0		,0			,96				,4		,0		,172,
	/*6H*/	0		,0		,0		,0		,0			,112			,4		,0		,172,
	/*7L*/	0		,0		,0		,0		,0			,40				,2		,0		,196,
	/*7M*/	0		,0		,0		,0		,0			,72				,4		,0		,196,
	/*7Q*/	0		,0		,0		,0		,0			,108			,2		,4		,196,
	/*7H*/	0		,0		,0		,0		,0			,130			,4		,1		,196,
	/*8L*/	0		,0		,0		,0		,0			,48				,2		,0		,242,
	/*8M*/	0		,0		,0		,0		,0			,88				,2		,2		,242,
	/*8Q*/	0		,0		,0		,0		,0			,132			,4		,2		,242,
	/*8H*/	0		,0		,0		,0		,0			,156			,4		,2		,242,
	/*9L*/	0		,0		,0		,0		,0			,60				,2		,0		,292,
	/*9M*/	0		,0		,0		,0		,0			,110			,3		,2		,292,
	/*9Q*/	0		,0		,0		,0		,0			,160			,4		,4		,292,
	/*9H*/	0		,0		,0		,0		,0			,192			,4		,4		,292,
	/*10L*/	0		,0		,0		,0		,0			,72				,2		,2		,346,
	/*10M*/	0		,0		,0		,0		,0			,130			,4		,1		,346,
	/*10Q*/	0		,0		,0		,0		,0			,192			,6		,2		,346,
	/*10H*/	0		,0		,0		,0		,0			,224			,6		,2		,346,
	/*11L*/	0		,0		,0		,0		,0			,80				,4		,0		,404,
	/*11M*/	0		,0		,0		,0		,0			,150			,1		,4		,404,
	/*11Q*/	0		,0		,0		,0		,0			,224			,4		,4		,404,
	/*11H*/	0		,0		,0		,0		,0			,264			,3		,8		,404,
	/*12L*/	0		,0		,0		,0		,0			,96				,2		,2		,466,
	/*12M*/	0		,0		,0		,0		,0			,176			,6		,2		,466,
	/*12Q*/	0		,0		,0		,0		,0			,260			,4		,6		,466,
	/*12H*/	0		,0		,0		,0		,0			,308			,7		,4		,466,
	/*13L*/	0		,0		,0		,0		,0			,104			,4		,0		,532,
	/*13M*/	0		,0		,0		,0		,0			,198			,8		,1		,532,
	/*13Q*/	0		,0		,0		,0		,0			,288			,8		,4		,532,
	/*13H*/	0		,0		,0		,0		,0			,352			,12		,4		,532,
	/*14L*/	0		,0		,0		,0		,0			,120			,3		,1		,581,
	/*14M*/	0		,0		,0		,0		,0			,216			,4		,5		,581,
	/*14Q*/	0		,0		,0		,0		,0			,320			,11		,5		,581,
	/*14H*/	0		,0		,0		,0		,0			,384			,11		,5		,581,
	/*15L*/	0		,0		,0		,0		,0			,132			,5		,1		,655,
	/*15M*/	0		,0		,0		,0		,0			,240			,5		,5		,655,
	/*15Q*/	0		,0		,0		,0		,0			,360			,5		,7		,655,
	/*15H*/	0		,0		,0		,0		,0			,432			,11		,7		,655,
	/*16L*/	0		,0		,0		,0		,0			,144			,5		,1		,733,
	/*16M*/	0		,0		,0		,0		,0			,280			,7		,3		,733,
	/*16Q*/	0		,0		,0		,0		,0			,408			,15		,2		,733,
	/*16H*/	0		,0		,0		,0		,0			,480			,3		,13		,733,
	/*17L*/	0		,0		,0		,0		,0			,168			,1		,5		,815,
	/*17M*/	0		,0		,0		,0		,0			,308			,10		,1		,815,
	/*17Q*/	0		,0		,0		,0		,0			,448			,1		,15		,815,
	/*17H*/	0		,0		,0		,0		,0			,532			,2		,17		,815,
	/*18L*/	0		,0		,0		,0		,0			,180			,5		,1		,901,
	/*18M*/	0		,0		,0		,0		,0			,338			,9		,4		,901,
	/*18Q*/	0		,0		,0		,0		,0			,504			,17		,1		,901,
	/*18H*/	0		,0		,0		,0		,0			,588			,2		,19		,901,
	/*19L*/	0		,0		,0		,0		,0			,196			,3		,4		,991,
	/*19M*/	0		,0		,0		,0		,0			,364			,3		,11		,991,
	/*19Q*/	0		,0		,0		,0		,0			,546			,17		,4		,991,
	/*19H*/	0		,0		,0		,0		,0			,650			,9		,16		,991,
	/*20L*/	0		,0		,0		,0		,0			,224			,3		,5		,1085,
	/*20M*/	0		,0		,0		,0		,0			,416			,3		,13		,1085,
	/*20Q*/	0		,0		,0		,0		,0			,600			,15		,5		,1085,
	/*20H*/	0		,0		,0		,0		,0			,700			,15		,10		,1085,
	/*21L*/	0		,0		,0		,0		,0			,224			,4		,4		,1156,
	/*21M*/	0		,0		,0		,0		,0			,442			,17		,0		,1156,
	/*21Q*/	0		,0		,0		,0		,0			,644			,17		,6		,1156,
	/*21H*/	0		,0		,0		,0		,0			,750			,19		,6		,1156,
	/*22L*/	0		,0		,0		,0		,0			,252			,2		,7		,1258,
	/*22M*/	0		,0		,0		,0		,0			,476			,17		,0		,1258,
	/*22Q*/	0		,0		,0		,0		,0			,690			,7		,16		,1258,
	/*22H*/	0		,0		,0		,0		,0			,816			,34		,0		,1258,
	/*23L*/	0		,0		,0		,0		,0			,270			,4		,5		,1364,
	/*23M*/	0		,0		,0		,0		,0			,504			,4		,4		,1364,
	/*23Q*/	0		,0		,0		,0		,0			,750			,11		,14		,1364,
	/*23H*/	0		,0		,0		,0		,0			,900			,16		,14		,1364,
	/*24L*/	0		,0		,0		,0		,0			,300			,6		,4		,1474,
	/*24M*/	0		,0		,0		,0		,0			,560			,6		,14		,1474,
	/*24Q*/	0		,0		,0		,0		,0			,810			,11		,16		,1474,
	/*24H*/	0		,0		,0		,0		,0			,960			,30		,2		,1474,
	/*25L*/	0		,0		,0		,0		,0			,312			,8		,4		,1588,
	/*25M*/	0		,0		,0		,0		,0			,588			,8		,13		,1588,
	/*25Q*/	0		,0		,0		,0		,0			,870			,7		,22		,1588,
	/*25H*/	0		,0		,0		,0		,0			,1050			,22		,13		,1588,
	/*26L*/	0		,0		,0		,0		,0			,336			,10		,2		,1706,
	/*26M*/	0		,0		,0		,0		,0			,644			,19		,4		,1706,
	/*26Q*/	0		,0		,0		,0		,0			,952			,28		,6		,1706,
	/*26H*/	0		,0		,0		,0		,0			,1110			,33		,4		,1706,
	/*27L*/	0		,0		,0		,0		,0			,360			,8		,4		,1828,
	/*27M*/	0		,0		,0		,0		,0			,700			,22		,3		,1828,
	/*27Q*/	0		,0		,0		,0		,0			,1020			,8		,26		,1828,
	/*27H*/	0		,0		,0		,0		,0			,1200			,12		,28		,1828,
	/*28L*/	0		,0		,0		,0		,0			,390			,3		,10		,1921,
	/*28M*/	0		,0		,0		,0		,0			,728			,3		,23		,1921,
	/*28Q*/	0		,0		,0		,0		,0			,1050			,4		,31		,1921,
	/*28H*/	0		,0		,0		,0		,0			,1260			,11		,31		,1921,
	/*29L*/	0		,0		,0		,0		,0			,420			,7		,7		,2051,
	/*29M*/	0		,0		,0		,0		,0			,784			,21		,7		,2051,
	/*29Q*/	0		,0		,0		,0		,0			,1140			,1		,37		,2051,
	/*29H*/	0		,0		,0		,0		,0			,1350			,19		,26		,2051,
	/*30L*/	0		,0		,0		,0		,0			,450			,5		,10		,2185,
	/*30M*/	0		,0		,0		,0		,0			,812			,19		,10		,2185,
	/*30Q*/	0		,0		,0		,0		,0			,1200			,15		,25		,2185,
	/*30H*/	0		,0		,0		,0		,0			,1440			,23		,25		,2185,
	/*31L*/	0		,0		,0		,0		,0			,480			,13		,3		,2323,
	/*31M*/	0		,0		,0		,0		,0			,868			,2		,29		,2323,
	/*31Q*/	0		,0		,0		,0		,0			,1290			,42		,1		,2323,
	/*31H*/	0		,0		,0		,0		,0			,1530			,23		,28		,2323,
	/*32L*/	0		,0		,0		,0		,0			,510			,17		,0		,2465,
	/*32M*/	0		,0		,0		,0		,0			,924			,10		,23		,2465,
	/*32Q*/	0		,0		,0		,0		,0			,1350			,10		,35		,2465,
	/*32H*/	0		,0		,0		,0		,0			,1620			,19		,35		,2465,
	/*33L*/	0		,0		,0		,0		,0			,540			,17		,1		,2611,
	/*33M*/	0		,0		,0		,0		,0			,980			,14		,21		,2611,
	/*33Q*/	0		,0		,0		,0		,0			,1440			,29		,19		,2611,
	/*33H*/	0		,0		,0		,0		,0			,1710			,11		,46		,2611,
	/*34L*/	0		,0		,0		,0		,0			,570			,13		,6		,2761,
	/*34M*/	0		,0		,0		,0		,0			,1036			,14		,23		,2761,
	/*34Q*/	0		,0		,0		,0		,0			,1530			,44		,7		,2761,
	/*34H*/	0		,0		,0		,0		,0			,1800			,59		,1		,2761,
	/*35L*/	0		,0		,0		,0		,0			,570			,12		,7		,2876,
	/*35M*/	0		,0		,0		,0		,0			,1064			,12		,26		,2876,
	/*35Q*/	0		,0		,0		,0		,0			,1590			,39		,14		,2876,
	/*35H*/	0		,0		,0		,0		,0			,1890			,22		,41		,2876,
	/*36L*/	0		,0		,0		,0		,0			,600			,6		,14		,3034,
	/*36M*/	0		,0		,0		,0		,0			,1120			,6		,34		,3034,
	/*36Q*/	0		,0		,0		,0		,0			,1680			,46		,10		,3034,
	/*36H*/	0		,0		,0		,0		,0			,1980			,2		,64		,3034,
	/*37L*/	0		,0		,0		,0		,0			,630			,17		,4		,3196,
	/*37M*/	0		,0		,0		,0		,0			,1204			,29		,14		,3196,
	/*37Q*/	0		,0		,0		,0		,0			,1770			,49		,10		,3196,
	/*37H*/	0		,0		,0		,0		,0			,2100			,24		,46		,3196,
	/*38L*/	0		,0		,0		,0		,0			,660			,4		,18		,3362,
	/*38M*/	0		,0		,0		,0		,0			,1260			,13		,32		,3362,
	/*38Q*/	0		,0		,0		,0		,0			,1860			,48		,14		,3362,
	/*38H*/	0		,0		,0		,0		,0			,2220			,42		,32		,3362,
	/*39L*/	0		,0		,0		,0		,0			,720			,20		,4		,3532,
	/*39M*/	0		,0		,0		,0		,0			,1316			,40		,7		,3532,
	/*39Q*/	0		,0		,0		,0		,0			,1950			,43		,22		,3532,
	/*39H*/	0		,0		,0		,0		,0			,2310			,10		,67		,3532,
	/*40L*/	0		,0		,0		,0		,0			,750			,19		,6		,3706,
	/*40M*/	0		,0		,0		,0		,0			,1372			,18		,31		,3706,
	/*40Q*/	0		,0		,0		,0		,0			,2040			,34		,34		,3706,
	/*40H*/	0		,0		,0		,0		,0			,2430			,20		,61		,3706
	};

	//////////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////英数対応表///////////////////////////////////////////////
			final static char[] eisuutaiou=	{'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z',' ','$','%','*','+','-','.','/',':'};

	//////////////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////GF(2^8)の指数表現の対応データベース ※添え字=整数　データ=aの係数//////////////////////
			final static int[] dataA={0,1,25,2,50,26,198,3,223,51,238,27,104,199,75,4,100,224,14,52,141,239,129,28,193,105,248,200,8,76,113,5,138,101,47,225,36,15,33,53,147,142,218,240,18,130,69,29,181,194,125,106,39,249,185,201,154,9,120,77,228,114,166,6,191,139,98,102,221,48,253,226,152,37,179,16,145,34,136,54,208,148,206,143,150,219,189,241,210,19,92,131,56,70,64,30,66,182,163,195,72,126,110,107,58,40,84,250,133,186,61,202,94,155,159,10,21,121,43,78,212,229,172,115,243,167,87,7,112,192,247,140,128,99,13,103,74,222,237,49,197,254,24,227,165,153,119,38,184,180,124,17,68,146,217,35,32,137,46,55,63,209,91,149,188,207,205,144,135,151,178,220,252,190,97,242,86,211,171,20,42,93,158,132,60,57,83,71,109,65,162,31,45,67,216,183,123,164,118,196,23,73,236,127,12,111,246,108,161,59,82,41,157,85,170,251,96,134,177,187,204,62,90,203,89,95,176,156,169,160,81,11,245,22,
				235,122,117,44,215,79,174,213,233,230,231,173,232,116,214,244,234,168,80,88,175};

	//////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////位置検出要素パターン//////////////////////////////////////////
			final static int[] POSITION_DETECTION_PATTERN =
				{
				  	1,1,1,1,1,1,1,
				  	1,2,2,2,2,2,1,
					1,2,1,1,1,2,1,
					1,2,1,1,1,2,1,
					1,2,1,1,1,2,1,
					1,2,2,2,2,2,1,
					1,1,1,1,1,1,1
				};


	//////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////位置合わせパターン//////////////////////////////////////////
			final static int[] ALIGNMENT_PATTERN=
				{
					1,1,1,1,1,
					1,2,2,2,1,
					1,2,1,2,1,
					1,2,2,2,1,
					1,1,1,1,1
				};

	//////////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////位置合わせパターン位置//////////////////////////////////////////
			final static int[][] ALIGNMENT_PATTERN_P=
				{
				/*Ver00*/{0},
				/*Ver01*/{0},
				/*Ver02*/{6,18},
				/*Ver03*/{6,22},
				/*Ver04*/{6,26},
				/*Ver05*/{6,30},
				/*Ver06*/{6,34},
				/*Ver07*/{6,22,38},
				/*Ver08*/{6,24,42},
				/*Ver09*/{6,26,46},
				/*Ver10*/{6,28,50},
				/*Ver11*/{6,30,54},
				/*Ver12*/{6,32,58},
				/*Ver13*/{6,34,62},
				/*Ver14*/{6,26,46,66},
				/*Ver15*/{6,26,48,70},
				/*Ver16*/{6,26,50,74},
				/*Ver17*/{6,30,54,78},
				/*Ver18*/{6,30,56,82},
				/*Ver19*/{6,30,58,86},
				/*Ver20*/{6,34,62,90},
				/*Ver21*/{6,28,50,72,94},
				/*Ver22*/{6,26,50,74,98},
				/*Ver23*/{6,30,54,78,102},
				/*Ver24*/{6,28,54,80,106},
				/*Ver25*/{6,32,58,84,110},
				/*Ver26*/{6,30,58,86,114},
				/*Ver27*/{6,34,62,90,118},
				/*Ver28*/{6,26,50,74,98,122},
				/*Ver29*/{6,30,54,78,102,126},
				/*Ver30*/{6,26,52,78,104,130},
				/*Ver31*/{6,30,56,82,108,134},
				/*Ver32*/{6,34,60,86,112,138},
				/*Ver33*/{6,30,58,86,114,142},
				/*Ver34*/{6,34,62,90,118,146},
				/*Ver35*/{6,30,54,78,102,126,150},
				/*Ver36*/{6,24,50,76,102,128,154},
				/*Ver37*/{6,28,54,80,106,132,158},
				/*Ver38*/{6,32,58,84,110,136,162},
				/*Ver39*/{6,26,54,82,110,138,166},
				/*Ver40*/{6,30,58,86,114,142,170},
				};

	//////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////型番情報//////////////////////////////////////////////
			final static String[] VERSION_INFORMATION={
			/*Ver07*/ "000111110010010100",
			/*Ver08*/ "001000010110111100",
			/*Ver09*/ "001001101010011001",
			/*Ver10*/ "001010010011010011",
			/*Ver11*/ "001011101111110110",
			/*Ver12*/ "001100011101100010",
			/*Ver13*/ "001101100001000111",
			/*Ver14*/ "001110011000001101",
			/*Ver15*/ "001111100100101000",
			/*Ver16*/ "010000101101111000",
			/*Ver17*/ "010001010001011101",
			/*Ver18*/ "010010101000010111",
			/*Ver19*/ "010011010100110010",
			/*Ver20*/ "010100100110100110",
			/*Ver21*/ "010101011010000011",
			/*Ver22*/ "010110100011001001",
			/*Ver23*/ "010111011111101100",
			/*Ver24*/ "011000111011000100",
			/*Ver25*/ "011001000111100001",
			/*Ver26*/ "011010111110101011",
			/*Ver27*/ "011011000010001110",
			/*Ver28*/ "011100110000011010",
			/*Ver29*/ "011101001100111111",
			/*Ver30*/ "011110110101110101",
			/*Ver31*/ "011111001001010000",
			/*Ver32*/ "100000100111010101",
			/*Ver33*/ "100001011011110000",
			/*Ver34*/ "100010100010111010",
			/*Ver35*/ "100011011110011111",
			/*Ver36*/ "100100101100001011",
			/*Ver37*/ "100101010000101110",
			/*Ver38*/ "100110101001100100",
			/*Ver39*/ "100111010101000001",
			/*Ver40*/ "101000110001101001"
			};




	//////////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////メソッド/////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////////////////////


	/////////////////////////////////////////////////////////////////////
	////////////////////基本データベース整理メソッド/////////////////////
	static int[][][] getVersionMaxLen(){
		//初期設定
			int[][][] versionMaxLen=new int[41][4][9];
			int tmp=0;
		//各型番のデータを配列に整理
			for (int i=1; i<=40; i++) {
				for (int j=0; j<4; j++) {
					for (int k=0; k<9; k++) {
						versionMaxLen[i][j][k] = maxLenDB[tmp];
						tmp++;
					}
				}
			}
		//各型番のデータコード数を計算
			for(int i=1;i<=40;i++){
				for(int j=0;j<4;j++){
					versionMaxLen[i][j][4]=versionMaxLen[i][j][8]-versionMaxLen[i][j][5];
				}
			}
		return versionMaxLen;
	}

	//////////////////////////////////////////////////////////////
	//////////////////2進数→10進数変換メソッド///////////////////
	static int get10 (String a){
		int b=Integer.parseInt(a, 2);
		return b;
	}

	//////////////////////////////////////////////////////////////
	/////////////////10進数→2進数変換メソッド////////////////////
	static String get2 (int a,int b){
		String c;
		c=Integer.toString(a, 2);
		for(int iii=c.length();iii<b;iii++){
			c="0"+c;
		}
		return c;
	}

	//////////////////////////////////////////////////////////////
	/////////////文字列からモードを取得するメソッド///////////////
	static int getEncodeMode (String str){
		int encodeMode=0;
		for(int i=0;i<str.length();i++){
			if(encodeMode<3){
				//数字かどうか (数字モード)
				if(str.charAt(i)>='0'&&str.charAt(i)<='9'){
				//英大文字かどうか(英数字モード)
				}else if(str.charAt(i)>='A' && str.charAt(i)<='Z'){
					if(encodeMode<2){
						encodeMode=1;
					}
				//半角記号かどうか(英数字モード)
				}else if(str.charAt(i)==' ' || str.charAt(i)=='$'||str.charAt(i)=='%'||str.charAt(i) == '*' || str.charAt(i) == '+' || str.charAt(i) == '-' || str.charAt(i) == '.' || str.charAt(i) == '/' || str.charAt(i) == ':') {
					if(encodeMode<2){
						encodeMode=1;
					}
				//英小文字かどうか(8bitバイトモード)
				} else if ((int)str.charAt(i)>=0 && (int)str.charAt(i)<=255) {
					encodeMode = 2;
				} else {
				//それ以外なら漢字モード
					encodeMode = 3;
				}
			}
		}
		//デバッグ用
			System.out.println("エンコードモード="+encodeMode);
		return encodeMode;
	}

	/////////////////////////////////////////////////////////////////////
	///////////////////モード指示子を取得するメソッド////////////////////
	static String getMode(int encodeMode){
		String modesikibetusi=null;
		switch (encodeMode) {
			case 0 :
				modesikibetusi = "0001";
				break;
			case 1 :
				modesikibetusi = "0010";
				break;
			case 2 :
				modesikibetusi = "0100";
				break;
			case 3 :
				modesikibetusi = "1000";
				break;
		}
		return modesikibetusi;
	}

	//////////////////////////////////////////////////////////////
	////////////////データの符号化をするメソッド//////////////////
	static String DtoB(String str,int encodeMode){
		//	データコードを格納する変数の初期化
			String str2="";
		////数字モードの場合////					  
			if (encodeMode == 0) {
				for (; ; ) {
					//データを3桁区切りで10bitで2進化。2桁しかない部分は7bit,1桁のとこは4bitで。
					if (str.length()>=3) {
						str2 += get2(Integer.parseInt(str.substring(0, 3)), 10);
						str = str.substring(3);
					} else if (str.length() == 2) {
						str2 += get2(Integer.parseInt(str.substring(0, 2)), 7);
						break;
					} else if (str.length() == 1) {
						str2 += get2(Integer.parseInt(str.substring(0, 1)), 4);
						break;
					} else {
						break;
					}
				}
			}
		////英数字モードの場合////
			if (encodeMode == 1) {
				int tmp;
				for (; ; ) {
					//データを2桁ずつに区切り、eisuutaiou[]の対応表を利用して2進化
					if (str.length()>=2) {
						tmp = eisuusearch(str.charAt(0))*45;
						tmp += eisuusearch(str.charAt(1));
						str2 += get2(tmp, 11);
						str = str.substring(2);
					} else if (str.length() == 1) {
						tmp = eisuusearch(str.charAt(0));
						str2 += get2(tmp, 6);
						break;
					} else {
						break;
					}
				}
			}
		////8bitバイトモードの場合////
			if (encodeMode == 2) {
				for (; str.length()>0; ) {
					//データのアスキーコードをそのまま2進化
					str2 += get2((int)str.charAt(0), 8);
					str = str.substring(1);
				}
			}
		////漢字モードの場合////
			//
			//
			// 未定義
			//
			//
		//改行コードのいらない部分を削除
			//str2=str2.replaceAll("00001101","");
		return str2;
	}

	//////////////////////////////////////////////////////////////
	////英数字を英数字モードの対応するコードに変換するメソッド/////
	static int eisuusearch(char a){
		for(int i=0;i<45;i++){
			if(eisuutaiou[i]==a){
				return i;
			}
		}
		System.out.println("エラー：　eisuusearch()　："+a);
		QRCode.info.setText(QRCode.info.getText()+"エラー：　eisuusearch()　："+a+BR);
		return -1;
	}

	/////////////////////////////////////////////////////////////////////
	////////////////シンボルのビットを反転させるメソッド/////////////////
	static void bitTurn(int s[][],int a,int b){
		if(s[a][b]==DATA_1){
			s[a][b]=DATA_0;
		}else if(s[a][b]==DATA_0){
			s[a][b]=DATA_1;
		}else if(s[a][b]==DATA_E1){
			s[a][b]=DATA_E0;	
		}else if(s[a][b]==DATA_E0){
			s[a][b]=DATA_E1;
		}else{
			System.out.println("エラー：　bitTurn()にて、引数の値が範囲を超えています a="+a+" b="+b);
			QRCode.info.setText(QRCode.info.getText()+"エラー：　bitTurn()にて、引数の値が範囲を超えています a="+a+" b="+b+BR);
		}
	}

	/////////////////////////////////////////////////////////////////////
	/////////文字数指示子の長さを取得するメソッド(a=ver,b=mode)//////////
	static int mozisusizisiLenGet(int ver,int encodeMode){
		int mozisusizisiLen=0;;
		if (ver>=1 && ver<=9) {
			switch (encodeMode) {
				case 0 :
				//バージョン1～9、数字モード (10bitで表す)
					mozisusizisiLen= 10;
					break;
				case 1 :
				//バージョン1～9、英数字モード (9bitで表す)
					mozisusizisiLen= 9;
					break;
				case 2 :
				//バージョン1～9、8bitモード (8bitで表す)
					mozisusizisiLen= 8;
					break;
				case 3 :
				//バージョン1～9、漢字モード (8bitで表す)
					mozisusizisiLen= 8;
			}
		}
		if (ver>=10 && ver<=26) {
			switch (encodeMode) {
				case 0 :
				//バージョン10～26、数字モード (12bitで表す)
					mozisusizisiLen= 12;
					break;
				case 1 :
				//バージョン10～26、英数字モード (11bitで表す)
					mozisusizisiLen= 11;
					break;
				case 2 :
				//バージョン10～26、8bitモード (16bitで表す)
					mozisusizisiLen= 16;
					break;
				case 3 :
				//バージョン10～26、漢字モード (10bitで表す)
					mozisusizisiLen= 10;
			}
		}
		if (ver>=27 && ver<=40) {
			switch (encodeMode) {
				case 0 :
				//バージョン27～40、数字モード (14bitで表す)
					mozisusizisiLen= 14;
					break;
				case 1 :
				//バージョン27～40、英数字モード (13bitで表す)
					mozisusizisiLen= 13;
					break;
				case 2 :
				//バージョン27～40、8bitモード (16bitで表す)
					mozisusizisiLen= 16;
					break;
				case 3 :
				//バージョン27～40、漢字モード (12bitで表す)
					mozisusizisiLen= 12;
			}
		}
		return mozisusizisiLen;
	}

	//////////////////////////////////////////////////////////////
	/////////////シンボルのトレースをするメソッド(int)////////////
	static void sTrace(int a[][],int simSize,String b){
		System.out.println(BR+"　===　"+b+"　===　");
		for(int i=0;i<simSize;i++){
			for(int j=0;j<simSize;j++){
				switch(a[i][j]){
					case NO_DATA:
						//データ未定義
						System.out.print("□");
						break;
					case BLACK_DATA:
						//黒データ
						System.out.print("■");
						break;
					case WHITE_DATA:
						//白データ
						System.out.print("◇");
						break;
					case K_DATA:
						//形式、型番情報が入る予定
						System.out.print("〇");
						break;
					case DATA_1:
						//データ1
						System.out.print("★");
						break;
					case DATA_0:
						//データ0
						System.out.print("☆");
						break;
					case DATA_VER:
						//型番情報
						System.out.print("◆");
						break;
					case DATA_K:
						//型番情報
						System.out.print("▲");
						break;
					case DATA_E1:
						//型番情報
						System.out.print("１");
						break;
					case DATA_E0:
						//型番情報
						System.out.print("０");
						break;
					default:
						//エラー
						System.out.println("エラー：　シンボルのデータがおかしいです。　："+a[i][j]);
						QRCode.info.setText(QRCode.info.getText()+"エラー：　シンボルのデータがおかしいです。　："+a[i][j]+BR);
				}
			}
			//改行
				System.out.println("");
		}
		System.out.println("□=データ無、■：黒データ、◇：白データ★：コード1");
		System.out.println("☆：コード0、◆：形式、▲：型番、○：形式型番予定地");
		System.out.println("１：エディタ絵前コード1、０：エディタ絵前コード0");
	}

	//////////////////////////////////////////////////////////////
	/////////シンボルのトレースをするメソッド(boolean)////////////
	static void sTrace(boolean a[][],int simSize,String b){
		System.out.println(BR+"　===　"+b+"　===　");
		for(int i=0;i<simSize;i++){
			for(int j=0;j<simSize;j++){
				if(a[i][j]==true){
					System.out.print("■");
				}else if(a[i][j]==false){
					System.out.print("□");
				}else{
					System.out.println("エラー：　シンボルのデータがおかしいです。　："+a[i][j]);
					QRCode.info.setText(QRCode.info.getText()+"エラー：　シンボルのデータがおかしいです。　："+a[i][j]+BR);
				}
			}
			//改行
				System.out.println("");
		}
	}

	//////////////////////////////////////////////////////////////
	/////////ビット列を数字モードで数値化するメソッド/////////////
	static String[] BtoD_mode0(String dataB){
		//初期設定
			String[] dataS;
			int tmp,tmp1;
		//変換処理
			if(dataB.length()%10>3){
				dataS=new String[(int)dataB.length()/10+1];
			}else{
				dataS=new String[(int)dataB.length()/10];
			}
			for(int i=0;i<(int)(dataB.length()/10);i++){
				dataS[i]=dataB.substring(i*10,i*10+10);
			}
			tmp=dataB.length()%10;
			if(tmp>=7){
				dataS[dataS.length-1]=dataB.substring(((dataS.length-2)*10),((dataS.length-2)*10)+7);
			}else if(tmp>=4){
				dataS[dataS.length-1]=dataB.substring(((dataS.length-2)*10),((dataS.length-2)*10)+4);
			}
			for(int i=0;i<dataS.length;i++){
				if(i<dataS.length-1 || tmp<4){
					dataS[i]=Integer.toString(DataBase.get10(dataS[i]));
					while(dataS[i].length()<3){
						dataS[i]="0"+dataS[i];
					}
					if(Integer.parseInt(dataS[i])>999){
						System.out.println("強制調節:999");
						dataS[i]="999";
					}
				}
				if(i==dataS.length-1 && tmp>3){
					tmp1=DataBase.get10(dataS[dataS.length-1]);
					if(dataS[i].length()==7 && tmp1>99){
						tmp=1;
						while(tmp1>99){
							tmp1=DataBase.get10(dataS[i].substring(0,7-tmp));
							System.out.println("強制調節:"+tmp1);
							tmp++;
						}
					}else if(dataS[i].length()==4 && tmp1>9){
						tmp=1;
						while(tmp1>9){
							tmp1=DataBase.get10(dataS[i].substring(0,4-tmp));
							System.out.println("強制調節:"+tmp1);
							tmp++;
						}
					}
					dataS[dataS.length-1]=Integer.toString(tmp1);
				}
			}
		return dataS;
	}

	///////////////////////////////////////////////////////////////////
	////////////////シンボルにマスクをかけるメソッド///////////////////	
	static void mask(int simbol[][],int mask,int simSize,int mode){
	//
	// mode=0:全て mode=1:データ前部分のみ mode=2:データ部分のみ
	//
		int[] tmp=new int[8];
		for(int i=0;i<8;i++){
			tmp[i]=0;
		}
		switch(mask){
			case 0:
				tmp[0]=1;
				break;
			case 1:
				tmp[1]=1;
				break;
			case 2:
				tmp[2]=1;
				break;
			case 3:
				tmp[3]=1;
				break;
			case 4:
				tmp[4]=1;
				break;
			case 5:
				tmp[5]=1;
				break;
			case 6:
				tmp[6]=1;
				break;
			case 7:
				tmp[7]=1;
				break;
			default:
				System.out.println("エラー：　マスクの値が異常です。");
				QRCode.info.setText(QRCode.info.getText()+"エラー：　マスクの値が異常です。"+BR);
		}
		for(int i=0;i<simSize;i++){
			for(int j=0;j<simSize;j++){
				if(mode==0 || mode==2){
					//現在のビットがデータコード部分ならマスク適用
						if(simbol[i][j]==DATA_1 || simbol[i][j]==DATA_0){
							if((((i+j)%2)*tmp[0])+((i%2)*tmp[1])+((j%3)*tmp[2])+(((i+j)%3)*tmp[3])+(((Math.floor(i/2)+Math.floor(j/3))%2)*tmp[4])+(((i*j) % 2 + (i*j) % 3)*tmp[5])+((((i*j) % 2 +(i*j) % 3) % 2)*tmp[6])+((((i*j) % 3 + (i+j) % 2) % 2)*tmp[7])==0){
								bitTurn(simbol,i,j);
							}
						}
				}
				if(mode==0 || mode==1){
					//現在のビットがエディタで使用する仮コード部分ならマスク適用
						if(simbol[i][j]==DATA_E1 || simbol[i][j]==DATA_E0){
							if((((i+j)%2)*tmp[0])+((i%2)*tmp[1])+((j%3)*tmp[2])+(((i+j)%3)*tmp[3])+(((Math.floor(i/2)+Math.floor(j/3))%2)*tmp[4])+(((i*j) % 2 + (i*j) % 3)*tmp[5])+((((i*j) % 2 +(i*j) % 3) % 2)*tmp[6])+((((i*j) % 3 + (i+j) % 2) % 2)*tmp[7])==0){
								bitTurn(simbol,i,j);
							}
						}
				}
			}
		}
	}

	//////////////////////////////////////////////////////////////
	////////////シンボルに固定データを入れるメソッド//////////////
	static void Indata(int ver,int simSize,int[][] simbol){
		//シンボルに初期化
			for(int i=0;i<simSize;i++){
				for(int j=0;j<simSize;j++){
					simbol[i][j]=NO_DATA;
				}
			}
		//シンボルに位置検出パターンを埋め込む
			//左上
				for(int i=0;i<7;i++){
					for(int j=0;j<7;j++){
						simbol[i][j]=POSITION_DETECTION_PATTERN[(i*7)+j];
					}
					simbol[i][7]=WHITE_DATA;
				}
				for(int i=0;i<8;i++){
					simbol[7][i]=WHITE_DATA;
				}
			//右上
				for(int i=0;i<7;i++){
					for(int j=0;j<7;j++){
						simbol[i][j+simSize-7]=POSITION_DETECTION_PATTERN[(i*7)+j];
					}
					simbol[i][simSize-8]=WHITE_DATA;
				}
				for(int i=0;i<8;i++){
					simbol[7][i+simSize-8]=WHITE_DATA;
				}
			//左下
				for(int i=0;i<7;i++){
					for(int j=0;j<7;j++){
						simbol[i+simSize-7][j]=POSITION_DETECTION_PATTERN[(i*7)+j];
					}
					simbol[i+simSize-7][7]=WHITE_DATA;
				}
				for(int i=0;i<8;i++){
					simbol[simSize-8][i]=WHITE_DATA;
				}
		//位置あわせパターンを挿入
			for(int i=0;i<ALIGNMENT_PATTERN_P[ver].length;i++){
				for(int j=0;j<ALIGNMENT_PATTERN_P[ver].length;j++){
					if(simbol[(ALIGNMENT_PATTERN_P[ver][i])][(ALIGNMENT_PATTERN_P[ver][j])]==NO_DATA){
						for(int ii=0;ii<5;ii++){
							for(int jj=0;jj<5;jj++){
								simbol[ALIGNMENT_PATTERN_P[ver][i]-2+ii][ALIGNMENT_PATTERN_P[ver][j]-2+jj]=ALIGNMENT_PATTERN[(ii*5)+jj];
							}
						}
					}else{
						//位置検出パターン等と重なっているのでスルー
					}
				}
			}
		//情報データが入る部分に空白を埋め込む
			//左上の形式情報
				for(int i=0;i<9;i++){
					simbol[8][i]=K_DATA;
					simbol[i][8]=K_DATA;
				}
			//右上の形式情報
				for(int i=0;i<8;i++){
					simbol[8][i+simSize-8]=K_DATA;
				}
			//左下の形式情報
				for(int i=simSize-7;i<simSize;i++){
					simbol[i][8]=K_DATA;
				}
		//型番情報の埋め込み
			if(ver>=7){
				String str="";
				for(int i=0;i<VERSION_INFORMATION[ver-7].length();i++){
					if(VERSION_INFORMATION[ver-7].charAt(i)=='1'){
						str+=Integer.toString(DATA_VER);
					}else{
						str+=Integer.toString(WHITE_DATA);
					}
				}
				for(int i=0;i<6;i++){
					for(int j=0;j<3;j++){
						//左下
							simbol[i][simSize-11+j]=Character.digit(str.charAt((i*3)+j),10);
						//右上
							simbol[simSize-11+j][i]=Character.digit(str.charAt((i*3)+j),10);
					}
				}
			}
		//タイミングパターンを埋め込む		  
			for(int i=0;i<(simSize-16);i++){
				if(i%2==1){
					simbol[6][i+8]=WHITE_DATA;
					simbol[i+8][6]=WHITE_DATA;
				}else{
					simbol[6][i+8]=BLACK_DATA;
					simbol[i+8][6]=BLACK_DATA;
				}
			}
			simbol[simSize-8][8]=BLACK_DATA;
	}

	//////////////////////////////////////////////////////////////
	//////////////2つの2進数をexor演算するメソッド////////////////
	static String exor (String a,String b){
		//2つのbit列の桁をそろえる
			for(;a.length()>b.length();){
				b="0"+b;
			}
			for(;b.length()>a.length();){
				a="0"+a;
			}
		//結果を格納する変数を初期化
			String c="";
		//排他的論理輪の計算
			for(int iii=0;iii<a.length();iii++){
				if(a.charAt(iii)==b.charAt(iii)){
					c+="0";
				}else{
					c+="1";
				}
			}
		return c;	
	}

	///////////////////////////////////////////////////////////////////
	///////////////形式情報をシンボルに埋め込むメソッド////////////////
	static void setFormatInformation(int[][] simbol,int lv,int mask){
		int[] ec;
		int[] ec2;
		int ecLen=0;
		int count=0;
		int simSize=simbol.length;
		String fiBitStr="";
		//誤り訂正レベルに応じたビットを付加
			switch(lv){
				case 0:
					fiBitStr+="01";
					break;
				case 1:
					fiBitStr+="00";
					break;
				case 2:
					fiBitStr+="11";
					break;
				case 3:
					fiBitStr+="10";
					break;
				default:
					System.out.println("エラー：　誤り訂正レベルが異常です。："+lv);
					QRCode.info.setText(QRCode.info.getText()+"エラー：　誤り訂正レベルが異常です。："+lv+BR);
			}
		//マスクに応じた形式情報ビットを付加
			switch(mask){
				case 0:
					fiBitStr+="000";
					break;
				case 1:
					fiBitStr+="001";
					break;
				case 2:
					fiBitStr+="010";
					break;
				case 3:
					fiBitStr+="011";
					break;
				case 4:
					fiBitStr+="100";
					break;
				case 5:
					fiBitStr+="101";
					break;
				case 6:
					fiBitStr+="110";
					break;
				case 7:
					fiBitStr+="111";
					break;
				default:
					System.out.println("エラー：　マスクの値が異常です。："+mask);
					QRCode.info.setText(QRCode.info.getText()+"エラー：　マスクの値が異常です。："+mask+BR);
			}
		//形式情報の誤り訂正コード生成
			//デバッグ用
				//System.out.println("[形式情報の誤り訂正コード生成開始]");
			//多項式生成
				ec=new int[40];
				ec2=new int[7];
				for(int i=0;i<40;i++){
					ec[i]=-1;
				}
				count=0;
				for(int i=0;i<5;i++){
					if(fiBitStr.charAt(i)=='1'){
						ec[count]=(4-i);
						count++;
					}
				}
				ecLen=count;
			//多項式を10倍
				for(int i=0;i<ecLen;i++){
					ec[i]+=10;
				}
			//デバッグ用
				//System.out.println("形式情報"+fiBitStr+"から");
				for(int i=0;i<ecLen;i++){
					//System.out.println("ec["+i+"]="+ec[i]);
				}
			//割り算ループ
				for(int i=0;ec[0]>=10;i++){
					//デバッグ用
						//System.out.println("除算ループ"+(i+1)+"周目");
					//ec2の初期化
						int[] ecD={10,8,5,4,2,1,0};
						for(int j=0;j<7;j++){
							ec2[j]=ecD[j];
						}
					count=ec[0]-10;
					for(int j=0;j<7;j++){
						ec2[j]+=count;
						ec[ecLen+j]=ec2[j];
					}
					java.util.Arrays.sort(ec);
					arrayOpposite(ec);
					//デバッグ用
						/*
						for(int ii=0;ii<ec.length;ii++){
							System.out.println("ec["+ii+"]="+ec[ii]);
						}
						*/
					//同じ値があれば削除
						for(int j=0;j<ec.length-1;j++){
							if(ec[j]!=-1){
								if(ec[j]==ec[j+1]){
									//デバッグ用
										//System.out.println("同じ数字があるので削除 :"+ec[j]);
									ec[j]=-1;
									ec[j+1]=-1;
									j=-1;
								}
							}
						}
					java.util.Arrays.sort(ec);
					arrayOpposite(ec);
					for(int j=0;j<ec.length;j++){
						if(ec[j]==-1){
							ecLen=j;
							break;
						}
					}
				}
				//デバッグ用
					/*
					System.out.print("余剰=");
					for(int ii=0;ii<ecLen;ii++){
						System.out.print(ec[ii]+",");
					}
					System.out.println("誤り訂正コードの計算終了");
					System.out.println("===============================");
					*/
			//多項式をビット列に変換
				java.util.Arrays.sort(ec);
				arrayOpposite(ec);
				count=0;
				for(int i=9;i>-1;i--){
					if(i==ec[count]){
						fiBitStr+="1";
						count++;
					}else{
						fiBitStr+="0";
					}
				}
			//EX-OR
				fiBitStr=exor(fiBitStr,"101010000010010");
			//デバッグ用
				//System.out.println("Mask="+mask+" fiBitStr="+fiBitStr);
			//データをシンボルに埋め込む用に変換
				int[] fiBit=new int[15];
				for(int i=0;i<15;i++){
					if(fiBitStr.charAt(i)=='1'){
						fiBit[i]=DATA_VER;
					}else{
						fiBit[i]=WHITE_DATA;
					}
				}
			//シンボルに埋め込む
				for(int i=0;i<6;i++){
					simbol[i][8]=fiBit[14-i];
					simbol[8][simSize-1-i]=fiBit[14-i];
				}
				simbol[7][8]=fiBit[14-6];
				simbol[8][8]=fiBit[14-7];
				simbol[8][simSize-1-7]=fiBit[14-7];
				simbol[8][simSize-1-6]=fiBit[14-6];
				for(int i=0;i<6;i++){
					simbol[8][i]=fiBit[i];
				}
				for(int i=0;i<7;i++){
					simbol[simSize-1-i][8]=fiBit[i];
				}
				simbol[8][7]=fiBit[14-8];
	}
	///////////////////////////////////////////////////////////////////
	////////////////整数配列の順序を逆にするメソッド///////////////////	
	static void arrayOpposite(int a[]){
		int len=(int)a.length/2;
		int tmp;
		for(int i=0;i<len;i++){
			tmp=a[i];
			a[i]=a[a.length-i-1];
			a[a.length-i-1]=tmp;
		}
	}

	///////////////////////////////////////////////////////////////////
	//////////ファイル入出力時のエラー時に実行するメソッド/////////////	
	static void ioError(){
		System.out.println("エラー：　ファイルの読み込みに失敗しました。");
		QRCode.info.setText(QRCode.info.getText()+"エラー：　ファイルの読み込みに失敗しました。"+BR);
	}
}