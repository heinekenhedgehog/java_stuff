import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PomodoroTimer extends JFrame {
    private JTextField studyTimeField;
    private JTextField breakTimeField;
    private JButton startButton;
    private JButton stopButton;
    private JButton resumeButton;
    private JLabel timerLabel;
    private Timer timer;
    private int remainingSeconds;
    private boolean onBreak = false;

    public PomodoroTimer() {
        setTitle("Pomodoro Timer");
        setSize(600, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main layout
        setLayout(new BorderLayout());

        // Left side: Tomato image (scaled)
        JLabel tomatoLabel = new JLabel();
        ImageIcon icon = new ImageIcon("tomato.png");
        Image scaled = icon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
        tomatoLabel.setIcon(new ImageIcon(scaled));
        tomatoLabel.setHorizontalAlignment(JLabel.CENTER);
        add(tomatoLabel, BorderLayout.WEST);

        // Right panel with input fields and buttons
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBackground(Color.BLACK);

        Font bigBoldFont = new Font("Arial", Font.BOLD, 18);

        JLabel studyLabel = new JLabel("Study Time (minutes):");
        studyLabel.setFont(bigBoldFont);
        studyLabel.setForeground(Color.WHITE);
        rightPanel.add(studyLabel);

        studyTimeField = new JTextField();
        studyTimeField.setFont(bigBoldFont);
        studyTimeField.setBackground(Color.BLACK);
        studyTimeField.setForeground(Color.WHITE);
        rightPanel.add(studyTimeField);

        JLabel breakLabel = new JLabel("Break Time (minutes):");
        breakLabel.setFont(bigBoldFont);
        breakLabel.setForeground(Color.WHITE);
        rightPanel.add(breakLabel);

        breakTimeField = new JTextField();
        breakTimeField.setFont(bigBoldFont);
        breakTimeField.setBackground(Color.BLACK);
        breakTimeField.setForeground(Color.WHITE);
        rightPanel.add(breakTimeField);

        startButton = new JButton("Start!");
        stopButton = new JButton("Stop");
        resumeButton = new JButton("Resume");

        styleButton(startButton, bigBoldFont);
        styleButton(stopButton, bigBoldFont);
        styleButton(resumeButton, bigBoldFont);

        stopButton.setEnabled(false);
        resumeButton.setEnabled(false);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.BLACK);
        buttonPanel.add(startButton);
        buttonPanel.add(stopButton);
        buttonPanel.add(resumeButton);

        rightPanel.add(Box.createVerticalStrut(20));
        rightPanel.add(buttonPanel);

        timerLabel = new JLabel("Timer: 00:00", SwingConstants.CENTER);
        timerLabel.setFont(new Font("Arial", Font.BOLD, 28));
        timerLabel.setForeground(Color.WHITE);
        timerLabel.setBackground(Color.BLACK);
        timerLabel.setOpaque(true);

        add(rightPanel, BorderLayout.CENTER);
        add(timerLabel, BorderLayout.SOUTH);

        // Timer logic
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int studyMinutes = Integer.parseInt(studyTimeField.getText());
                    int breakMinutes = Integer.parseInt(breakTimeField.getText());

                    remainingSeconds = studyMinutes * 60;
                    onBreak = false;

                    startTimer(studyMinutes, breakMinutes);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Please enter valid numbers.");
                }
            }
        });

        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (timer != null) {
                    timer.stop();
                    stopButton.setEnabled(false);
                    resumeButton.setEnabled(true);
                }
            }
        });

        resumeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (timer != null) {
                    timer.start();
                    stopButton.setEnabled(true);
                    resumeButton.setEnabled(false);
                }
            }
        });
    }

    private void styleButton(JButton button, Font font) {
        button.setFont(font);
        button.setBackground(Color.BLACK);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
    }

    private void startTimer(int studyMinutes, int breakMinutes) {
        if (timer != null) {
            timer.stop();
        }

        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (remainingSeconds > 0) {
                    remainingSeconds--;
                    updateTimerLabel();
                } else {
                    if (!onBreak) {
                        JOptionPane.showMessageDialog(null, "Time for a break!");
                        remainingSeconds = breakMinutes * 60;
                        onBreak = true;
                    } else {
                        JOptionPane.showMessageDialog(null, "Break is over! Back to study.");
                        remainingSeconds = studyMinutes * 60;
                        onBreak = false;
                    }
                }
            }
        });

        timer.start();
        stopButton.setEnabled(true);
        resumeButton.setEnabled(false);
    }

    private void updateTimerLabel() {
        int minutes = remainingSeconds / 60;
        int seconds = remainingSeconds % 60;
        timerLabel.setText(String.format("Timer: %02d:%02d", minutes, seconds));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new PomodoroTimer().setVisible(true);
        });
    }
}

