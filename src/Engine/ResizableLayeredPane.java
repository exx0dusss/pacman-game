package Engine;

import javax.swing.*;
import java.awt.*;
import java.io.Serial;

public class ResizableLayeredPane extends JLayeredPane {
    @Serial
    private static final long serialVersionUID = 1L;

    @Override
    public void doLayout() {
        super.doLayout();
        synchronized (getTreeLock()) {
            int w = getWidth();
            int h = getHeight();
            for (Component c : getComponents()) {
                if (getLayer(c) == JLayeredPane.PALETTE_LAYER) {
                    c.setFont(Core.getMainFont());
                }
                c.setBounds(0, 0, w, h);
            }
        }
    }
}
