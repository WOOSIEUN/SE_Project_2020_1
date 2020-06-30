public class MoyeoraScheduler {
	MainFrame mainFrame; //메인프레임
	Login loginFrame; //로그인화면
	Register RegisterFrame; //회원가입 화면

	public static void main(String[] args) {
		//PropertyManagementSystemDemo 클래스 실행
		MoyeoraScheduler Moyeora = new MoyeoraScheduler();
		Moyeora.loginFrame = new Login(); // 로그인창 보이기
		Moyeora.loginFrame.setMain(Moyeora); // 로그인창에게 데모 클래스보내기		
	}
	
	//회원가입창 띄우는 메소드
	public void showRegisterFrame(){
		this.RegisterFrame = new Register();
		RegisterFrame.setVisible(true);
	}
		
	//메인프레임 띄우는 메소드
	public void showMainFrame(String ID, String Name, boolean isMaster){
		loginFrame.dispose(); // 로그인창닫기
		this.mainFrame = new MainFrame(ID, Name, isMaster); //메인프레임 생성
	}
}