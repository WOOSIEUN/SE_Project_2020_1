public class MoyeoraScheduler {
	MainFrame mainFrame; //����������
	Login loginFrame; //�α���ȭ��
	Register RegisterFrame; //ȸ������ ȭ��

	public static void main(String[] args) {
		//PropertyManagementSystemDemo Ŭ���� ����
		MoyeoraScheduler Moyeora = new MoyeoraScheduler();
		Moyeora.loginFrame = new Login(); // �α���â ���̱�
		Moyeora.loginFrame.setMain(Moyeora); // �α���â���� ���� Ŭ����������		
	}
	
	//ȸ������â ���� �޼ҵ�
	public void showRegisterFrame(){
		this.RegisterFrame = new Register();
		RegisterFrame.setVisible(true);
	}
		
	//���������� ���� �޼ҵ�
	public void showMainFrame(String ID, String Name, boolean isMaster){
		loginFrame.dispose(); // �α���â�ݱ�
		this.mainFrame = new MainFrame(ID, Name, isMaster); //���������� ����
	}
}