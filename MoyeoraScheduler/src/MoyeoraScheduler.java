
public class MoyeoraScheduler {
	MainFrame mainFrame; //메인프레임

	public static void main(String[] args) {
		//PropertyManagementSystemDemo 클래스 실행
		MoyeoraScheduler Moyeora = new MoyeoraScheduler();
		Moyeora.showMainFrame("cyj_1201", "최유진", false);
	}
	
	//메인프레임 띄우는 메소드
	public void showMainFrame(String ID, String Name, boolean isMaster){
		this.mainFrame = new MainFrame(ID, Name, isMaster); //메인프레임 생성
	}

}
