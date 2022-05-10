package ru.nsu.kudryavtsev.andrey;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Method;
import java.security.InvalidParameterException;
import java.util.HashMap;

/**
 * MainFrame - main application frame
 * Application should subclass it to create its own implementation
 * @author Tagir F. Valeev
 */
public class MainFrame extends JFrame {
    private static final long serialVersionUID = 1L;
    private JMenuBar menuBar;
    protected JToolBar toolBar;
    public final HashMap<String, ButtonGroup> radioButtonGroups = new HashMap<>();
    public final HashMap<String, ButtonGroup> toggleButtonGroups = new HashMap<>();

    /**
     * Default constructor which sets up L&F and creates tool-bar and menu-bar
     */
    public MainFrame()
    {
        try
        {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        }
        catch(Exception e)
        {
            throw new RuntimeException("Unable to set look and feel");
        }
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        toolBar = new JToolBar("Main toolbar");
        toolBar.setRollover(true);
        add(toolBar, BorderLayout.PAGE_START);
    }

    /**
     * Shortcut method to create menu item
     * Note that you have to insert it into proper place by yourself
     * @param title - menu item title
     * @param tooltip - floating tooltip describing menu item
     * @param mnemonic - mnemonic key to activate item via keyboard
     * @param icon - file name containing icon (must be located in 'resources' subpackage relative to your implementation of MainFrame), can be null
     * @param actionMethod - String containing method name which will be called when menu item is activated (method should not take any parameters)
     * @return created menu item
     * @throws NoSuchMethodException - when actionMethod method not found
     * @throws SecurityException - when actionMethod method is inaccessible
     */
    public JMenuItem createMenuItem(String title, String tooltip, int mnemonic, String icon, String actionMethod) throws SecurityException, NoSuchMethodException
    {
        JMenuItem item = new JMenuItem(title);
        item.setMnemonic(mnemonic);
        item.setToolTipText(tooltip);
        if(icon != null)
            item.setIcon(new ImageIcon(getClass().getResource("/"+icon), title));
        final Method method = getClass().getMethod(actionMethod, ActionEvent.class);
        item.addActionListener(evt -> {
            try {
                method.invoke(MainFrame.this, evt);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        return item;
    }

    public JMenuItem createRadioButtonMenuItem(String title, String tooltip, int mnemonic, String icon, String actionMethod)
            throws SecurityException, NoSuchMethodException
    {
        JRadioButtonMenuItem item = new JRadioButtonMenuItem(title);
        item.setActionCommand(title);
        item.setMnemonic(mnemonic);
        item.setToolTipText(tooltip);
        if (icon != null)
            item.setIcon(new ImageIcon(getClass().getResource("/"+icon), title));
        final Method method = getClass().getMethod(actionMethod, ActionEvent.class);
        item.addActionListener(evt -> {
            try {
                selectRelatedButton("Tools", (AbstractButton) evt.getSource());
                method.invoke(MainFrame.this, evt);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        return item;
    }

    /**
     * Shortcut method to create menu item (without icon)
     * Note that you have to insert it into proper place by yourself
     * @param title - menu item title
     * @param tooltip - floating tooltip describing menu item
     * @param mnemonic - mnemonic key to activate item via keyboard
     * @param actionMethod - String containing method name which will be called when menu item is activated (method should not take any parameters)
     * @return created menu item
     * @throws NoSuchMethodException - when actionMethod method not found
     * @throws SecurityException - when actionMethod method is inaccessible
     */
    public JMenuItem createMenuItem(String title, String tooltip, int mnemonic, String actionMethod) throws SecurityException, NoSuchMethodException
    {
        return createMenuItem(title, tooltip, mnemonic, null, actionMethod);
    }

    /**
     * Creates submenu and returns it
     * @param title - submenu title
     * @param mnemonic - mnemonic key to activate submenu via keyboard
     * @return created submenu
     */
    public JMenu createSubMenu(String title, int mnemonic, String icon)
    {
        JMenu menu = new JMenu(title);
        menu.setMnemonic(mnemonic);
        if(icon != null)
            menu.setIcon(new ImageIcon(getClass().getResource("/"+icon), title));
        return menu;
    }

    /**
     * Creates submenu and inserts it to the specified location
     * @param title - submenu title with full path (just submenu title for top-level submenus)
     * example: "File/New" - will create submenu "New" under menu "File" (provided that menu "File" was previously created)
     * @param mnemonic - mnemonic key to activate submenu via keyboard
     */
    public void addSubMenu(String title, int mnemonic, String icon)
    {
        MenuElement element = getParentMenuElement(title);
        if(element == null)
            throw new InvalidParameterException("Menu path not found: "+title);
        JMenu subMenu = createSubMenu(getMenuPathName(title), mnemonic, icon);
        if(element instanceof JMenuBar)
            ((JMenuBar)element).add(subMenu);
        else if(element instanceof JMenu)
            ((JMenu)element).add(subMenu);
        else if(element instanceof JPopupMenu)
            ((JPopupMenu)element).add(subMenu);
        else
            throw new InvalidParameterException("Invalid menu path: "+title);
    }

    public void addSubMenu(String title, int mnemonic) {
        addSubMenu(title, mnemonic, null);
    }

    /**
     * Creates menu item and adds it to the specified menu location
     * @param title - menu item title with full path
     * @param tooltip - floating tooltip describing menu item
     * @param mnemonic - mnemonic key to activate item via keyboard
     * @param icon - file name containing icon (must be located in 'resources' subpackage relative to your implementation of MainFrame), can be null
     * @param actionMethod - String containing method name which will be called when menu item is activated (method should not take any parameters)
     * @throws NoSuchMethodException - when actionMethod method not found
     * @throws SecurityException - when actionMethod method is inaccessible
     * @throws InvalidParameterException - when specified menu location not found
     */
    public void addMenuItem(String title, String tooltip, int mnemonic, String icon, String actionMethod) throws SecurityException, NoSuchMethodException
    {
        MenuElement element = getParentMenuElement(title);
        if(element == null)
            throw new InvalidParameterException("Menu path not found: "+title);
        JMenuItem item = createMenuItem(getMenuPathName(title), tooltip, mnemonic, icon, actionMethod);
        if(element instanceof JMenu)
            ((JMenu)element).add(item);
        else if(element instanceof JPopupMenu)
            ((JPopupMenu)element).add(item);
        else
            throw new InvalidParameterException("Invalid menu path: "+title);
    }

    /**
     * Creates menu item (without icon) and adds it to the specified menu location
     * @param title - menu item title with full path
     * @param tooltip - floating tooltip describing menu item
     * @param mnemonic - mnemonic key to activate item via keyboard
     * @param actionMethod - String containing method name which will be called when menu item is activated (method should not take any parameters)
     * @throws NoSuchMethodException - when actionMethod method not found
     * @throws SecurityException - when actionMethod method is inaccessible
     * @throws InvalidParameterException - when specified menu location not found
     */
    public void addMenuItem(String title, String tooltip, int mnemonic, String actionMethod) throws SecurityException, NoSuchMethodException
    {
        addMenuItem(title, tooltip, mnemonic, null, actionMethod);
    }

    public void addRadioButtonMenuItem(String title, String group, String tooltip, int mnemonic, String icon, String actionMethod)
            throws SecurityException, NoSuchMethodException {
        MenuElement element = getParentMenuElement(title);
        if(element == null) {
            throw new InvalidParameterException("Menu path not found: "+title);
        }
        if (!radioButtonGroups.containsKey(group)) {
            radioButtonGroups.put(group, new ButtonGroup());
        }
        JMenuItem item = createRadioButtonMenuItem(getMenuPathName(title), tooltip, mnemonic, icon, actionMethod);
        radioButtonGroups.get(group).add(item);
        if(element instanceof JMenu)
            ((JMenu)element).add(item);
        else if(element instanceof JPopupMenu)
            ((JPopupMenu)element).add(item);
        else
            throw new InvalidParameterException("Invalid menu path: "+title);
    }

    public void addRadioButtonMenuItem(String title, String group, String tooltip, int mnemonic, String actionMethod)
            throws SecurityException, NoSuchMethodException {
        addRadioButtonMenuItem(title, group, tooltip, mnemonic, null, actionMethod);
    }

    /**
     * Adds menu separator in specified menu location
     * @param title - menu location
     * @throws InvalidParameterException - when specified menu location not found
     */
    public void addMenuSeparator(String title)
    {
        MenuElement element = getMenuElement(title);
        if(element == null)
            throw new InvalidParameterException("Menu path not found: "+title);
        if(element instanceof JMenu)
            ((JMenu)element).addSeparator();
        else if(element instanceof JPopupMenu)
            ((JPopupMenu)element).addSeparator();
        else
            throw new InvalidParameterException("Invalid menu path: "+title);
    }

    private String getMenuPathName(String menuPath)
    {
        int pos = menuPath.lastIndexOf('/');
        if(pos > 0)
            return menuPath.substring(pos+1);
        else
            return menuPath;
    }

    /**
     * Looks for menu element by menu path ignoring last path component
     * @param menuPath - '/'-separated path to menu item (example: "Help/About...")
     * @return found menu item or null if no such item found
     */
    private MenuElement getParentMenuElement(String menuPath)
    {
        int pos = menuPath.lastIndexOf('/');
        if(pos > 0)
            return getMenuElement(menuPath.substring(0, pos));
        else
            return menuBar;
    }

    /**
     * Looks for menu element by menu path
     * @param menuPath - '/'-separated path to menu item (example: "Help/About...")
     * @return found menu item or null if no such item found
     */
    public MenuElement getMenuElement(String menuPath)
    {
        MenuElement element = menuBar;
        for(String pathElement: menuPath.split("/"))
        {
            MenuElement newElement = null;
            for(MenuElement subElement: element.getSubElements())
            {
                if((subElement instanceof JMenu && ((JMenu)subElement).getText().equals(pathElement))
                        || (subElement instanceof JMenuItem && ((JMenuItem)subElement).getText().equals(pathElement)))
                {
                    if(subElement.getSubElements().length==1 && subElement.getSubElements()[0] instanceof JPopupMenu)
                        newElement = subElement.getSubElements()[0];
                    else
                        newElement = subElement;
                    break;
                }
            }
            if(newElement == null) return null;
            element = newElement;
        }
        return element;
    }

    /**
     * Creates toolbar button which will behave exactly like specified menuitem
     * @param item - menuitem to create toolbar button from
     * @return created toolbar button
     */
    public JButton createToolBarButton(JMenuItem item)
    {
        JButton button = new JButton(item.getIcon());
        for(ActionListener listener: item.getActionListeners())
            button.addActionListener(listener);
        button.setToolTipText(item.getToolTipText());
        return button;
    }

    public JToggleButton createToolBarToggleButton(JMenuItem item, String group) {
        JToggleButton button = new JToggleButton(item.getIcon());
        button.setActionCommand(item.getText());
        for(ActionListener listener: item.getActionListeners())
            button.addActionListener(evt -> {
                selectRelatedButton("Tools", (AbstractButton) evt.getSource());
                listener.actionPerformed(evt);
            });
        button.setToolTipText(item.getToolTipText());
        toggleButtonGroups.get(group).add(button);
        return button;
    }

    private void selectRelatedButton(String selectedButtonGroupName, AbstractButton selectedButton) {
        ButtonGroup relatedButtonGroup;

        if (radioButtonGroups.get(selectedButtonGroupName).isSelected(selectedButton.getModel())) {
            relatedButtonGroup = toggleButtonGroups.get(selectedButtonGroupName);
        } else if (toggleButtonGroups.get(selectedButtonGroupName).isSelected(selectedButton.getModel())) {
            relatedButtonGroup = radioButtonGroups.get(selectedButtonGroupName);
        } else {
            throw new RuntimeException("Radio or toggle button without group");
        }

        var iter = relatedButtonGroup.getElements().asIterator();
        while (iter.hasNext()) {
            var relatedButton = iter.next();
            if (relatedButton.getActionCommand().equals(selectedButton.getActionCommand())) {
                relatedButtonGroup.setSelected(relatedButton.getModel(), true);
                return;
            }
        }
    }

    /**
     * Creates toolbar button which will behave exactly like specified menuitem
     * @param menuPath - path to menu item to create toolbar button from
     * @return created toolbar button
     */
    public JButton createToolBarButton(String menuPath)
    {
        JMenuItem item = (JMenuItem)getMenuElement(menuPath);
        if(item == null)
            throw new InvalidParameterException("Menu path not found: "+menuPath);
        return createToolBarButton(item);
    }

    public JToggleButton createToolBarToggleButton(String menuPath, String group) {
        JMenuItem item = (JMenuItem)getMenuElement(menuPath);
        if(item == null)
            throw new InvalidParameterException("Menu path not found: "+menuPath);
        if (!toggleButtonGroups.containsKey(group)) {
            toggleButtonGroups.put(group, new ButtonGroup());
        }
        return createToolBarToggleButton(item, group);
    }

    /**
     * Creates toolbar button which will behave exactly like specified menuitem and adds it to the toolbar
     * @param menuPath - path to menu item to create toolbar button from
     */
    public void addToolBarButton(String menuPath)
    {
        toolBar.add(createToolBarButton(menuPath));
    }

    public void addToolBarToggleButton(String menuPath, String group) {
        toolBar.add(createToolBarToggleButton(menuPath, group));
    }

    /**
     * Adds separator to the toolbar
     */
    public void addToolBarSeparator()
    {
        toolBar.addSeparator();
    }
}
