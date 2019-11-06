package xiaolei.gao.git;

@SuppressWarnings("serial")
public class MyGitExcepition extends Exception{

	public MyGitExcepition(){}

    public MyGitExcepition(String message)
    {
        super(message);
    }
}
