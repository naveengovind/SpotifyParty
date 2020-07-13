package gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import static gui.SpotifyPartyPanelChat.spfc;

public class Chat extends JPanel {
    public static JTextPane chat;
    private static BufferedImage icon;
    public JViewport chatViewPort;

    public Chat() {
        this.setOpaque(false);

        putClientProperty("Aqua.backgroundStyle", "vibrantUltraDark");
        try {
            icon = ImageIO.read(Notification.class.getResource("/images/logo.png"));
        } catch (
                IOException e) {
            e.printStackTrace();
        }
        this.setLayout(null);

        chat = new JTextPane();
        chat.setFocusable(false);
        chat.setAutoscrolls(true);
        chat.setOpaque(false);
        chat.setEditable(false);
        JScrollPane chatScroll = new JScrollPane();
        chatViewPort = chatScroll.getViewport();
        chatScroll.getViewport().setView(chat);
        chatScroll.setBounds(20, 0, 410, 460);
        chatScroll.setBorder(new EmptyBorder(0, 0, 0, 0));
        chatScroll.setOpaque(false);
        chatScroll.getViewport().setOpaque(false);
        chatScroll.getVerticalScrollBar().setPreferredSize(new Dimension(0, 517));
        chatScroll.getVerticalScrollBar().setOpaque(false);
        chatScroll.getVerticalScrollBar().setBorder(new EmptyBorder(0, 0, 0, 0));
        chatScroll.getVerticalScrollBar().setUnitIncrement(16);
        chatScroll.getVerticalScrollBar().setBackground(new Color(30, 30, 30));
        chatScroll.getVerticalScrollBar().setPreferredSize(new Dimension(5, 300));
        chatScroll.setAutoscrolls(true);
        chatViewPort.setAutoscrolls(true);
        this.add(chatScroll);
        /*
        try {
            UIManager.setLookAndFeel("org.violetlib.aqua.AquaLookAndFeel");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

         */
    }

    public String prev = "";
    public boolean you;
    public void addText(String text, String name) {
        text = reformat(text);

        if(!spfc.isActive()) {
            System.out.println("hi");
            Notification notification = new Notification(icon, "SpotifyParty", name, text, 5000);
            notification.addActivationListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    spfc.toFront();
                    spfc.requestFocus();
                }
            });
            notification.send();
        }
        StyledDocument doc = chat.getStyledDocument();
        SimpleAttributeSet left = new SimpleAttributeSet();
        SimpleAttributeSet right = new SimpleAttributeSet();
        StyleConstants.setAlignment(left, StyleConstants.ALIGN_LEFT);
        StyleConstants.setAlignment(right, StyleConstants.ALIGN_RIGHT);
        // doc.setParagraphAttributes(0, doc.getLength(), left, false);
        chat.setFont(new Font("CircularSpUIv3T-Bold", Font.PLAIN, 20));
        Style style = chat.addStyle("I'm a Style", null);
        if(!prev.equals(name)) {
            if(name.equals(SpotifyPartyPanelChat.FriendName)) {
                you = true;
                StyleConstants.setForeground(style, Color.GREEN);
            } else {
                you = false;
                StyleConstants.setForeground(style, Color.GRAY);
            }
            StyleConstants.setFontSize(style, 20);

            try {
                doc.insertString(doc.getLength(), "\n" + name + "\n",style);
                if(you)
                    doc.setParagraphAttributes(doc.getLength() - ("\n" + name + "\n").length(), doc.getLength(), right, false);
                else
                    doc.setParagraphAttributes(doc.getLength() - ("\n" + name + "\n").length(), doc.getLength(), left, false);
            }
            catch (BadLocationException e){}
        }

        StyleConstants.setForeground(style, Color.WHITE);
        StyleConstants.setFontSize(style, 15);

        try {
            doc.insertString(doc.getLength(),  text + "\n",style);
            if(you)
                doc.setParagraphAttributes(doc.getLength() - (text + "\n").length(), doc.getLength(), right, false);
            else
                doc.setParagraphAttributes(doc.getLength() - (text + "\n").length(), doc.getLength(), left, false);
        }
        catch (BadLocationException e){}

        chatViewPort.setViewPosition(new Point(0, Integer.MAX_VALUE/4));
        prev = name;
    }

    public String reformat(String text) {
        int space = 34;
        while(space < text.length()) {
            int counter = 0;
            while(text.charAt(space) != ' ' && counter < 4 && space < text.length()) {
                space++;
                counter++;
            }
            counter = 0;
            text = text.substring(0, space) + "\n" + text.substring(space);
            space += 34;
        }
        return text;
    }
}