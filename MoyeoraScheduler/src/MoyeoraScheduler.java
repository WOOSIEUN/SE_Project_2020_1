
public class MoyeoraScheduler {
	MainFrame mainFrame; //����������

	public static void main(String[] args) {
		//PropertyManagementSystemDemo Ŭ���� ����
		MoyeoraScheduler Moyeora = new MoyeoraScheduler();
		Moyeora.showMainFrame("cyj_1201", "������", false);
	}
	
	//���������� ���� �޼ҵ�
	public void showMainFrame(String ID, String Name, boolean isMaster){
		this.mainFrame = new MainFrame(ID, Name, isMaster); //���������� ����
	}

}
