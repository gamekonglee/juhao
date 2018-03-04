package bc.juhao.com.ui.view.designview;

public class ImageObject
{
  private int colorRes;
  private String imageName;
  
  public ImageObject(int paramInt, String paramString)
  {
    this.colorRes = paramInt;
    this.imageName = paramString;
  }
  
  public String getImageName()
  {
    return this.imageName;
  }
  
  public int getShotRes()
  {
    return this.colorRes;
  }
  
  public void setColorRes(int paramInt)
  {
    this.colorRes = paramInt;
  }
  
  public void setImageName(String paramString)
  {
    this.imageName = paramString;
  }
}