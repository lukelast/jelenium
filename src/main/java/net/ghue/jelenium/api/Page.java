package net.ghue.jelenium.api;

public abstract class Page extends InjectBase {

   /**
    * Load this page.
    */
   public final void go() {
      this.go( getContext().getWebNavigate() );
   }

   /**
    * Navigate to this page.
    * 
    * @param navigate Used to navigate to web pages.
    */
   protected abstract void go( WebNavigate navigate );

}
