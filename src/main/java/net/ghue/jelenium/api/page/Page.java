package net.ghue.jelenium.api.page;

import javax.inject.Inject;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public abstract class Page extends InjectBase {

   /**
    * Initializes {@link WebElement} fields. Override this to disable the feature.
    * 
    * @see FindBy
    */
   @Inject
   protected void initPageFactory() {
      PageFactory.initElements( this.getDriver(), this );
   }

   protected WebNavigate navigate() {
      return getContext().getWebNavigate();
   }

}
