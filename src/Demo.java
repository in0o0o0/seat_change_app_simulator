
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

class Train  extends JFrame{
	static final int LOW=15;
	static final int COLUMN=5;
	final int MAX=LOW*COLUMN;
	static final int TERMINAL =5;
	final int COUNT=1;
	int changeCount=0;
	int [] check = new int[TERMINAL];//各駅で何回アプリでの交代があったか
	 int energy=0;
	int rideNumber;
	int now=0;
	Passenger [][] pa;
	int sitNum=0;
	int standNum=0;
	int rideNum;
	int total=0;
	int a,h;
	int age;
	int health;
	public void setState(int state) {
		this.state = state;
	}

	int state;
	int flag=0;
	JPanel panel;
	JButton button;
	JLabel station,label;
	Random rnd = new Random();
	ArrayList<Passenger> p = new ArrayList<Passenger>();
	ArrayList<JButton> b = new ArrayList<JButton>();
	
	
  Train(int n){
  	pa   = new Passenger[COLUMN][LOW];
  	
  	this.setLayout(null);    	
  	this.setBounds(50,50+300*n,50*LOW,COLUMN*50+20);   
      this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      this.setVisible(true); 
   }

  public void addEnergy(Passenger p){
  	if(p.status==0)
  		energy+=p.fatigue;
  	else
  		energy+=p.fatigue*2;   	
  }
  
  public void makePassenger(int column,int low,int getOff,int fatigue){
  	b.add(new JButton());
  	p.add(new Passenger(column,low,now,getOff,fatigue,b.get(total)));
  	add(b.get(total));
  	pa[column][low]=p.get(total);
		pa[p.get(total).column][p.get(total).low]=p.get(total);
		p.get(total).getOn();
		total++; 
	}
  //譲ってもいい人を探す
  public void useApp(){
  	for(int i=0;i<LOW;i++)
  		try{
  		if(pa[0][i].give==1 && pa[0][i].app==1){//譲ってもいい人がいた
  			searchPartnerA(pa[0][i]);
  		}}catch(NullPointerException e){				
			}
  	
  	for(int i=0;i<LOW;i++)
  		try{
  		if(pa[COLUMN-1][i].give==1 && pa[0][i].app==1)//譲ってもいい人がいた
  			searchPartnerB(pa[COLUMN-1][i]);
  		}catch(NullPointerException e){				
			}catch(ArrayIndexOutOfBoundsException e){}   	
  }
  //代わる相手を探す
  public void searchPartnerA(Passenger p){
  	int c=p.column;
  	int l=p.low;
  	
  	for(int i=1;i<COLUMN-1;i++)
  		for(int j=0;j<5;j++)
  			try{
  			if(pa[c+i][l-1+j]!=null && pa[c+i][l-1+j].recieve==1 && pa[c+i][l-1+j].app==1)
  				p.choosePartner(pa[c+i][l-1+j]);  			
  			}catch(ArrayIndexOutOfBoundsException e){				
  			}
  	
  	if(p.giveFlag==1)
  		changeSeat(p);
  }
  public void searchPartnerB(Passenger p){
  	int c=p.column;
  	int l=p.low;
  	
  	for(int i=1;i<COLUMN-1;i++)
  		for(int j=0;j<3;j++)
  			try{
  			if(pa[c-i][l-1+j]!=null && pa[c-i][l-1+j].recieve==1){
  				p.choosePartner(pa[c-i][l-1+j]);
  			}
  			}catch(ArrayIndexOutOfBoundsException e){				
  			}
  	if(p.giveFlag==1)
  		changeSeat(p);
  }
  //アプリを使って変わる時に使う
  public void changeSeat(Passenger p){
  	int column=p.column;
		int low=p.low;
		p.move(p.partner.column, p.partner.low);
		pa[p.partner.column][p.partner.low]=p;
		p.partner.move(column,low);	
		pa[column][low]=p.partner;
		p.setStatus(1);//譲った人は立つ
		p.partner.setStatus(0);//譲ってもらったひとは座る
		p.setRecieve(2);//席を譲った人は席を欲しがらない
		p.partner.setGive(-1);//譲ってもらった人は譲らない
		p.partner.setColor();
		p.giveFlag=0;
  }

  public void arriveNextStation(){
  	now++;
  	for(int i=0;i<COLUMN;i++){
  		for(int j=0;j<LOW;j++){
  			if(pa[i][j]==null)
  				;
  			else{
  				addEnergy(pa[i][j]);
  			  if(pa[i][j].getOffSta == now){
  				pa[i][j].move(COLUMN/2,LOW);
  				pa[i][j]=null;  
  				if(i==0 || i==COLUMN-1)//座っていた人が降りる
  				sitNum--;  				
  				else 
  					standNum--;
  			}
  			}
  		}
  	}
  	if(now == TERMINAL){
  		flag=1;
  	}
  }
   public void searchVacantSeat(){
  	for(int i=0;i<LOW;i++)
			if(pa[0][i]==null){
				sitSeatA(0,i);
			}
		
		for(int i=0;i<LOW;i++)
			if(pa[COLUMN-1][i]==null){
				sitSeatB(COLUMN-1,i);
			}
	}
    
   public void setData(Passenger p){
   	p.setRecieve(0);
		p.setGive(1);
		p.setStatus(0);
	}
   
  public void sitSeatA(int x,int y){
  	LABEL: for(int i=0;i<COLUMN-2;i++)
  	do{
   	try{
			if(pa[x+1+i][y]!=null){
				pa[x+1+i][y].move(x,y);
				setData(pa[x+1+i][y]);
				pa[x][y]=pa[x+1+i][y];
				pa[x+1+i][y]=null;
				sitNum++;
				standNum--;
				break LABEL;
			}
		}catch(ArrayIndexOutOfBoundsException e){
		}
  	try{
			if(pa[x+1+i][y+1]!=null){
				pa[x+1+i][y+1].move(x, y);
				setData(pa[x+1+i][y+1]);
				pa[x][y]=pa[x+1+i][y+1];
				pa[x+1+i][y+1]=null;
				sitNum++;
				standNum--;
				break LABEL;
			}
		}catch(ArrayIndexOutOfBoundsException e){
		}
  	try{
			if(pa[x+1+i][y-1]!=null){
				pa[x+1+i][y-1].move(x, y);
				setData(pa[x+1+i][y-1]);
				pa[x][y]=pa[x+1+i][y-1];
				pa[x+1+i][y-1]=null;
				sitNum++;
				standNum--;
				break LABEL;
			}
		}catch(ArrayIndexOutOfBoundsException e){
		}
  	if(i==1){
      	try{
  			if(pa[x+1+i][y-2]!=null){
  				pa[x+1+i][y-2].move(x, y);
  				setData(pa[x+1+i][y-2]);
  				pa[x][y]=pa[x+1+i][y-2];
  				pa[x+1+i][y-2]=null;
  				sitNum++;
  				standNum--;
  				break LABEL;
  			}
  		}catch(ArrayIndexOutOfBoundsException e){
  		}
      	try{
  			if(pa[x+1+i][y+2]!=null){
  				pa[x+1+i][y+2].move(x, y);
  				pa[x+1+i][y+2].setRecieve(0);
  				pa[x+1+i][y+2].setGive(1);
  				pa[x+1+i][y+2].setStatus(0);
  				pa[x][y]=pa[x+1+i][y+2];
  				pa[x+1+i][y+2]=null;
  				sitNum++;
  				standNum--;
  				break LABEL;
  			}
  		}catch(ArrayIndexOutOfBoundsException e){
  		}
      	}
  	}while(false);
  	
  
  }
  
  public void sitSeatB(int x,int y){
  	LABEL:for(int i=0;i<COLUMN-2;i++)
  	do{
  	try{
			if(pa[x-1-i][y]!=null){
				pa[x-1-i][y].move(x,y);
				setData(pa[x-1-i][y]);
				pa[x][y]=pa[x-1-i][y];
				pa[x-1-i][y]=null;
				sitNum++;
				standNum--;
				break LABEL;
			}
		}catch(ArrayIndexOutOfBoundsException e){
		}
  	try{
			if(pa[x-1-i][y+1]!=null){
				pa[x-1-i][y+1].move(x, y);
				setData(pa[x-1-i][y+1]);
				pa[x][y]=pa[x-1-i][y+1];
				pa[x-1-i][y+1]=null;
				sitNum++;
				standNum--;
				break LABEL;
			}
		}catch(ArrayIndexOutOfBoundsException e){
		}
  	try{
			if(pa[x-1-i][y-1]!=null){
				pa[x-1-i][y-1].move(x, y);
				setData(pa[x-1-i][y-1]);
				pa[x][y]=pa[x-1-i][y-1];
				pa[x-1-i][y-1]=null;
				sitNum++;
				standNum--;
				break LABEL;
			}
		}catch(ArrayIndexOutOfBoundsException e){
		}
  	if(i==1){
  	try{
			if(pa[x-1-i][y-2]!=null){
				pa[x-1-i][y-2].move(x, y);
				setData(pa[x-1-i][y-2]);
				pa[x][y]=pa[x-1-i][y-2];
				pa[x-1-i][y-2]=null;
				sitNum++;
				standNum--;
				break LABEL;
			}
		}catch(ArrayIndexOutOfBoundsException e){
		}
  	try{
			if(pa[x-1-i][y+2]!=null){
				pa[x-1-i][y+2].move(x, y);
				setData(pa[x-1-i][y+2]);
				pa[x][y]=pa[x-1-i][y+2];
				pa[x-1-i][y+2]=null;
				sitNum++;
				standNum--;
				break LABEL;
			}
		}catch(ArrayIndexOutOfBoundsException e){
		}
  	}
  	}while(false);
  	
  }
	
	
}


class Passenger implements ActionListener{

	int rideSta;//乗る駅
	int getOffSta;//降りる駅
	int status;//座っているか立っているか 0座っている 1立っている
	int fatigue;
	int app=1;//アプリを持っているか
	int give=1;//席を譲ってもいいか 0NO 1YES -1席を譲ってもらったので人には譲らない
	int giveFlag;
	int recieve=1;//席に座りたいか 0NO 1YES 2相手が-1席を譲ったので立った（座らない）
	int point;//0~9のランダム
	int column;//何行目にいるか
	int low;//何列目にいるか
	int newColumn;//移動用	
	int newLow;//移動用
	int x,y,t,t1,t2;//移動用
	Passenger partner=null;//席を交代する相手（座っている方が使う）
	Timer getOn;
	Timer move;
	JButton b;

	
	Passenger(int column,int low,int now,int getOffSta,int fatigue, JButton b){
		this.column = column;
		this.low = low;
		this.rideSta = now;
		this.getOffSta = getOffSta;
		this.fatigue = fatigue;
		this.b = b;
		b.addActionListener(this);
		b.setText(""+fatigue+"");
		this.b.setBounds(450,0,50,50);
		if(column==0 || column==4){
			status=0;
			give=1;
			recieve=0;
		}else{
			status=1;
			give=0;
			recieve=1;
		}
		getOn = new Timer(30,this);
		getOn.setActionCommand("on");
		move = new Timer(30,this);
		move.setActionCommand("move");
	}
	
	
	 public void choosePartner(Passenger p){
		 if(fatigue >=p.fatigue)
			 ;
		 else if(partner==null){//最初の一人
			 partner = p;
	    		giveFlag=1;  
		 }else if(partner.fatigue <p.fatigue){
			 partner = p;
			 giveFlag=1; 
		 }
		 }
	public void setGive(int give) {
		this.give = give;
	}
	public void setRecieve(int recieve) {
		this.recieve = recieve;
		if(recieve==2)
			b.setForeground(Color.blue);
	}
	
	public void setColumn(int column) {
		this.column = column;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	public void setLow(int low) {
		this.low = low;
	}

	public void  getOn(){
				if(column==0)
					y=-10;
				else if(column==4)
					y=10;
				else if(column==1)
					y=-5;
				else if(column==2)
					;
				else 
					y=5;
				getOn.start();	
	}
	public void move(int newC,int newL){
		t1=Math.abs(newC-column)*5;
		if(newL-low>0)
			x=10;
		else 
			x=-10;
		if(newC-column>0)
			y=10;
		else 
			y=-10;
		t2=Math.abs(newL-low)*5;
		move.start();
		newColumn=newC;
		newLow=newL;
		if(newC==0 || newC==4)
			status=0;
		else
			status=1;
	}
	public void setColor(){
		b.setForeground(Color.red);
	}
	public void feeling(){
		
	}
	
	
	public void actionPerformed(ActionEvent e){
		String es=e.getActionCommand();
		if(es.equals("on")){
		if(t<=low*5){
			b.setBounds(t*10,100,50,50);
			t++;
		}else if(t>low*5 && t<=low*5+10){
			b.setBounds(50*low,100+y*(t-low*5),50,50);
			t++;
		}else{
			getOn.stop();
			t=0;
		}
		}else if(es.equals("move")){
			if(t<=t1){
				b.setBounds(50*low,50*column+y*t,50,50);
				t++;
			}else if(t<=t2+t1){
				b.setBounds(50*low+x*(t-t1),column*50+t1*y,50,50);
				t++;
			}else {
				move.stop();
				t=0;
				setLow(newLow);
				setColumn(newColumn);
			}			
		}else;
		}
			
} 



 class Demo extends JFrame implements ActionListener{
    	Train []t =new Train[2];
    	int a,h;
    	int age;
    	int health;
    	int fatigue;
    	int column;
    	int low;
    	int getOff;
    	int state=1;//状態
    	int [] fatigueNum;//何人ずついるか
    	JButton b;
    	JLabel l1,l2,l3,e1,e2,e3;
    	Random rnd = new Random();
    	
    	Demo(){

    		 t[0]=new Train(0);
    		 t[1]=new Train(1);

    		 
    		for(int j=0;j<t[0].LOW*2;j++){
    			decideAge();
 				decideHealth();
 				fatigue=age*health;
 	    		decideGetOffSta();
 	    		chooseSeat(t[0],t[1]);
 	    		t[0].makePassenger(column,low,getOff,fatigue);
 	    		t[1].makePassenger(column,low,getOff,fatigue); 	 
 	    		
 			}
    		decidePassenger(t[0],t[1],0);
    		 	
    		
    		b=new JButton("アプリを使う");
    		b.addActionListener(this);
    		b.setBounds(0, 0, 300, 100);
    		this.add(b);
    		this.setLayout(null);
    		this.pack();
    		this.setBounds(1100,100,300,120);   
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            this.setVisible(true); 
    	}
    	public static void main(String args[]){
        	new Demo();
        	}
    	
    	public void start(Train t,Train t2){
    		for(int i=0;i<t.COUNT;i++){	
    			
    			for(int j=0;j<t.LOW*2;j++){
    				decideAge();
    				decideHealth();
    				fatigue=age*health;
    	    		decideGetOffSta();
    	    		chooseSeat(t);
    	    		t.makePassenger(column,low,getOff,fatigue);
    	    		chooseSeat(t2);
    	    		t2.makePassenger(column,low,getOff,fatigue); 
    	    		
    			}		
    			
    		    	  decidePassenger(t,t2);
    		    	  t.useApp(); 
    		    	  t.arriveNextStation();
    		    	  //t2.useApp(); 
    		  		  t2.arriveNextStation();
    		  		 		    
    		  		  //ここをアクションイベントで行うんだな
    			for(int k=1;k<t.TERMINAL;k++){
    			t.searchVacantSeat();
    			t2.searchVacantSeat();
    			decidePassenger(t,t2);
    			t.useApp();
    			 // t2.useApp();
    			 
    			t.arriveNextStation();
    			t2.arriveNextStation();
    			}   			 				
    		}//ここまでCOUNTループ
    	}
    	   public void decidePassenger(Train t,Train t2){
    		   if(t.MAX-(t.sitNum+t.standNum)==0)
    		    ;
    	    	else{
    	    		t.rideNum=t.rnd.nextInt(t.MAX-(t.sitNum+t.standNum));
    	    	for(int j=0;j<t.rideNum;j++){
    				decideAge();
    	    		decideHealth();
    	    		fatigue=age*health;
    	    		decideGetOffSta();
    	    		chooseSeat(t);
    				t.makePassenger(column,low,getOff,fatigue);
    				chooseSeat(t2);
    				t2.makePassenger(column,low,getOff,fatigue);
    	    	}
    	    	}
    	     }
    	   
    	   //同じ行列でつくる最初のみ
    	   public void decidePassenger(Train t,Train t2,int n){
    		   if(t.MAX-(t.sitNum+t.standNum)==0)
    		    ;
    	    	else{
    	    		t.rideNum=t.rnd.nextInt(t.MAX-(t.sitNum+t.standNum));
    	    	for(int j=0;j<t.rideNum;j++){
    				decideAge();
    	    		decideHealth();
    	    		fatigue=age*health;
    	    		decideGetOffSta();
    	    		chooseSeat(t);
    				t.makePassenger(column,low,getOff,fatigue);
    				t2.makePassenger(column,low,getOff,fatigue);
    	    	}
    	    	}
    	     }
    	 	public void decideGetOffSta(){
    	 		getOff= t[0].rnd.nextInt(t[0].TERMINAL-t[0].now)+t[0].now+1;
    		 }
    	    public void decideAge(){
    	    	a =rnd.nextInt(100);
    	    	if(a<17)
    	    		age=10;
    	    	else if(a>=17 && a<40)
    	    		age=12;
    	    	else if(a>=40 && a<74)
    	    		age=15;
    	    	else if(a>=74 && a<100)
    	    		age=20;
    	    }
    	    public void decideHealth(){
    	    	h =rnd.nextInt(100);
    	    	if(h<35)
    	    		health=8;
    	    	else if(h>=35 && h<89)
    	    		health=10;
    	    	else if(h>=89 && h<99)
    	    		health=15;
    	    	else if(h>=99 && h<100)
    	    		health=20;
    	    } 
    	    
    	  //乗ってきた人が席を選ぶ(席が空いていたら座る)　それぞれのTrainで呼び出す
    	    public void chooseSeat(Train t){
    			int n,m;
    			if(t.sitNum>=t.LOW*2){//座れない
    				do{
    					n=t.rnd.nextInt(t.COLUMN-2)+1;
    					m=t.rnd.nextInt(t.LOW);
    				}while(t.pa[n][m]!=null);
    				column=n;//コンストラクタに渡す
    				low=m;
    				t.standNum++;
    			}else {//座れる
    				do{
    					n=t.rnd.nextInt(2);
    					if(n==1)
    						n=t.COLUMN-1;
    					m=t.rnd.nextInt(t.LOW);	
    				}while(t.pa[n][m]!=null);
    				column=n;
    				low=m;
    				t.sitNum++;
    		}					
    		}
    	    
    	    //全く同じ席を作る最初だけ使う
    	    public void chooseSeat(Train t,Train t2){
    			int n,m;
    			if(t.sitNum>=t.LOW*2){//座れない
    				do{
    					n=t.rnd.nextInt(t.COLUMN-2)+1;
    					m=t.rnd.nextInt(t.LOW);
    				}while(t.pa[n][m]!=null);
    				column=n;//コンストラクタに渡す
    				low=m;
    				t.standNum++;
    				t2.standNum++;
    			}else {//座れる
    				do{
    					n=t.rnd.nextInt(2);
    					if(n==1)
    						n=t.COLUMN-1;
    					m=t.rnd.nextInt(t.LOW);	
    				}while(t.pa[n][m]!=null);
    				column=n;
    				low=m;
    				t.sitNum++;
    				t2.sitNum++;
    		}					
    		}
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if(t[0].flag==0){
					if(state==0){//乗車
						decidePassenger(t[0],t[1]);
						state=1;
					}else if(state==1){//アプリ						
						t[0].useApp();		
						state=2;
					}else if(state==2){//降車		
						t[0].arriveNextStation();
						t[1].arriveNextStation();
						//changeText();
						state=3;
					}else if(state==3){//席替え
						t[0].searchVacantSeat();
						t[1].searchVacantSeat();
						state=0;
					}
					}else;
				changeButton();
			}
			
			public void changeButton(){
				if(t[0].flag==0){
				if(state==0)
					b.setText("乗車");
				else if(state==1)
					b.setText("アプリを使う");
				else if(state==2)
					b.setText("降車");
				else if(state==3)
					b.setText("席替え");
				}else
					b.setText("終点です");
			}

}
