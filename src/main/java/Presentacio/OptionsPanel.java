package Presentacio;

import Domini.*;
import javax.swing.*;
import java.awt.*;

public class OptionsPanel extends JPanel {
    private ControladorPresentacio cp;
    private ControladorDomini cd;

    public OptionsPanel(ControladorPresentacio cp, ControladorDomini cd) {
        this.cp = cp;
        this.cd = cd;
        initialize();
    }

    private void initialize() {
        setLayout(new BorderLayout());
        setBackground(new Color(240, 240, 240));

        JPanel optionsContent = new JPanel(new GridLayout(0, 1));
        optionsContent.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton changePassButton = new JButton("Change Password");
        CommonComponents.styleButton(changePassButton, new Color(76, 175, 80));
        changePassButton.addActionListener(e -> changePassword());
        optionsContent.add(changePassButton);

        JButton deleteAccountButton = new JButton("Delete Account");
        CommonComponents.styleButton(deleteAccountButton, new Color(239, 83, 80));
        deleteAccountButton.addActionListener(e -> deleteAccount());
        optionsContent.add(deleteAccountButton);

        add(optionsContent, BorderLayout.CENTER);

        JButton backButton = new JButton("Back to Menu");
        CommonComponents.styleButton(backButton, new Color(66, 165, 245));
        backButton.addActionListener(e -> cp.showMainMenuPanel());
        add(backButton, BorderLayout.SOUTH);
    }

    private void changePassword() {
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPasswordField currentPassField = new JPasswordField(15);
        JPasswordField newPassField = new JPasswordField(15);
        JPasswordField confirmPassField = new JPasswordField(15);

        panel.add(new JLabel("Current Password:"));
        panel.add(currentPassField);
        panel.add(new JLabel("New Password:"));
        panel.add(newPassField);
        panel.add(new JLabel("Confirm New Password:"));
        panel.add(confirmPassField);

        int result = JOptionPane.showConfirmDialog(cp.getFrame(), panel, "Change Password", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String currentPass = new String(currentPassField.getPassword());
            String newPass = new String(newPassField.getPassword());
            String confirmPass = new String(confirmPassField.getPassword());

            if (!newPass.equals(confirmPass)) {
                JOptionPane.showMessageDialog(cp.getFrame(), "New passwords don't match", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                cd.restablirContrasenya(cp.getCurrentUser().getNom(), currentPass, newPass);
                JOptionPane.showMessageDialog(cp.getFrame(), "Password changed successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(cp.getFrame(), ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteAccount() {
        int confirm = JOptionPane.showConfirmDialog(cp.getFrame(),
                "Are you sure you want to delete your account? This cannot be undone.",
                "Confirm Account Deletion",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            cd.eliminarCompte(cp.getCurrentUser().getNom());
            cp.setCurrentUser(null);
            JOptionPane.showMessageDialog(cp.getFrame(), "Account deleted successfully", "Info", JOptionPane.INFORMATION_MESSAGE);
            cp.showLoginPanel();
        }
    }
}