package Controller.Admin.PopUp;

import Controller.AbstractController;
import org.controlsfx.control.PopOver;

/**
 * Created by benhylak on 2/8/17.
 */
public class OfficeEditController extends AbstractPopupController
{
    protected PopOver popOver;

    public OfficeEditController(PopOver popOver)
    {
        super(popOver);
    }

    public void onConfirm()
    {
        popOver.hide();
    }
}
