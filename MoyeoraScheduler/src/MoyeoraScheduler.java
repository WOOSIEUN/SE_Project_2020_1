
public class MoyeoraScheduler {
	MainFrame mainFrame; //����������

	public static void main(String[] args) {
		//PropertyManagementSystemDemo Ŭ���� ����
		MoyeoraScheduler Moyeora = new MoyeoraScheduler();
		Moyeora.showMainFrame("ramtk6726", "�����", false);
	}
	
	//���������� ���� �޼ҵ�
	public void showMainFrame(String ID, String Name, boolean isMaster){
		this.mainFrame = new MainFrame(ID, Name, isMaster); //���������� ����
	}

}
