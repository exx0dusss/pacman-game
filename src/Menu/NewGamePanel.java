package Menu;

import Engine.Core;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.plaf.basic.ComboPopup;
import java.awt.*;

public class NewGamePanel extends JPanel {
    private static final int WIDTH, HEIGHT;
    private final GridBagConstraints gbc;
    private final MenuUpperPanel upperPanel;
    private static int panelHeight;
    private final int minOption;
    private final int maxOption;
    private final String[] options;
    private static JPanel selectPanel;
    private final JLabel selectionLabel;
    private final NewGameComboBox rowsBox;
    private final NewGameComboBox columnsBox;
    private final MenuFrame parent;
    private final MenuButton sumbitButton;


    static {
        WIDTH = MenuFrame.getMenuWidth();
        HEIGHT = MenuFrame.getMenuHeight();
    }

    NewGamePanel(MenuFrame parent) {
        this.parent = parent;
        minOption = 10;
        maxOption = 100;
        options = new String[maxOption - minOption + 1];
        for (int i = 10; i <= maxOption; i++) {
            options[i - 10] = String.valueOf(i);
        }

        upperPanel = new MenuUpperPanel(parent);
        panelHeight = (int) upperPanel.getPreferredSize().getHeight();

        selectionLabel = new JLabel();
        selectionLabel.setText("Select row/column quantity: ");
        selectionLabel.setForeground(Color.white);
        selectionLabel.setFont(Core.getMainFont());

        rowsBox = new NewGameComboBox(options);
        columnsBox = new NewGameComboBox(options);
        sumbitButton = new MenuButton();
        sumbitButton.setPreferredSize(new Dimension(
                244, 45));
        sumbitButton.setText("Submit");
        sumbitButton.addActionListener(parent.getMenuListener());

        selectPanel = new JPanel();
        selectPanel.setPreferredSize(
                new Dimension(WIDTH, HEIGHT - panelHeight));
        selectPanel.setBackground(Color.black);
        selectPanel.setOpaque(false);
        selectPanel.setFont(Core.getMainFont());
        selectPanel.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(18, 0, 18, 0);

        gbc.gridy = 1;
        selectPanel.add(selectionLabel, gbc);

        gbc.gridy = 2;
        selectPanel.add(rowsBox, gbc);

        gbc.gridy = 3;
        selectPanel.add(columnsBox, gbc);

        gbc.gridy = 4;
        selectPanel.add(sumbitButton, gbc);

        this.setSize(new Dimension(WIDTH, HEIGHT));
        this.setLayout(new BorderLayout());
        this.setOpaque(false);
        this.add(upperPanel, BorderLayout.NORTH);
        this.add(selectPanel, BorderLayout.CENTER);
    }

    public MenuUpperPanel getUpperPanel() {
        return upperPanel;
    }

    public MenuButton getSumbitButton() {
        return sumbitButton;
    }

    public NewGameComboBox getRowsBox() {
        return rowsBox;
    }

    public NewGameComboBox getColumnsBox() {
        return columnsBox;
    }

    public class NewGameComboBox extends JComboBox<String> {

        Color mainColor = MenuButton.getMainColor();
        Color extraColor = MenuButton.getExtraColor();
        Border boxBorder = BorderFactory.createEtchedBorder(mainColor, extraColor);

        NewGameComboBox(String[] options) {
            super(options);
            this.setRenderer(new MyComboBoxRenderer());
            this.setBackground(Core.getBlueColor());
            this.setForeground(Color.white);
            this.setOpaque(false);
            this.setSelectedIndex(25 - minOption);
            this.setFont(Core.getMainFont());
            this.setBorder(boxBorder);
            this.setUI(new CustomComboBoxUI());
            this.setFocusable(false);
        }

        class MyComboBoxRenderer extends DefaultListCellRenderer {
            public MyComboBoxRenderer() {
                setOpaque(true);
                setHorizontalAlignment(CENTER);
                setVerticalAlignment(CENTER);
                setBackground(Core.getBlueColor());
            }

            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
                                                          boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected,
                        cellHasFocus);

                label.setPreferredSize(new Dimension(200, 40));
                if (isSelected) {
                    label.setBackground(Color.white);
                    label.setForeground(Core.getBlueColor());

                } else {
                    label.setBackground(Core.getBlueColor());
                    label.setForeground(Color.white);
                }

                return label;
            }

        }

        public class CustomComboBoxUI extends BasicComboBoxUI {
            @Override
            protected ComboPopup createPopup() {
                BasicComboPopup popup = (BasicComboPopup) super.createPopup();
                JScrollPane scrollPane = (JScrollPane) popup.getComponent(0);

                scrollPane.setBorder(BorderFactory.createLineBorder(new Color(255, 204, 0))); // Set border
                int policy = scrollPane.getVerticalScrollBarPolicy();
                if (policy == ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS
                        || policy == ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED) {
                    scrollPane.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
                        @Override
                        protected void configureScrollBarColors() {
                            this.thumbColor = new Color(255, 204, 0);
                            this.trackColor = Core.getBlueColor();
                        }


                        @Override
                        protected JButton createDecreaseButton(int orientation) {
                            return new JButton() {
                                @Override
                                public Dimension getPreferredSize() {
                                    return new Dimension(0, 0);
                                }
                            };
                        }

                        @Override
                        protected JButton createIncreaseButton(int orientation) {
                            return new JButton() {
                                @Override
                                public Dimension getPreferredSize() {
                                    return new Dimension(0, 0);
                                }
                            };
                        }
                    });
                }
                return popup;
            }


            @Override
            protected JButton createArrowButton() {
                return new JButton() {
                    public int getWidth() {
                        return 0;
                    }
                };
            }
        }

    }

}
