
package socketpunch;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.swing.DefaultListModel;


/**
 *
 * @author Josh Johansson
 * Thanks to Steveice10 for FBI and sockfile
 */



public final class Main extends javax.swing.JFrame {
    String CONST_PATH = System.getProperty("user.dir") + "\\log.txt";
    String CONST_SETTING_PATH = System.getProperty("user.dir") + "\\config.txt";
    String NEWLINE = System.getProperty("line.separator");

    DefaultListModel model = new DefaultListModel();
    private DefaultListModel queueModel = new DefaultListModel();
    boolean startedSending = false;
    int transferCount = 0;
    int failCount = 0;
    int bufferSize = 128;
    public Main() {
        setTitle( "SocketPunch v0.3.4 - Joshtech" );
        initComponents();
        // Get local ip and set ipText value
        try {
            InetAddress addr = InetAddress.getLocalHost();
            ipText.setText(addr.getHostAddress());
        } catch (UnknownHostException ex) {
            consoleWrite("There appears to be a problem with the network!", true);
        }
        GetSettings();
        // We check if the log file exists
        if(saveLogChk.isSelected()){
        // Create 3 newlines in log if there is already a log. Try to keep things seperate
        File file = new File(CONST_PATH);
        // if file doesnt exists, then create it
        if (file.exists()) {
        	for(int i = 0; i < 3; i++){
                    WriteLog(CONST_PATH, "");
                }
        }
        }
        // Print some stoof
        consoleWrite("==============================", false);
        consoleWrite("                 SocketPunch v0.3.4", false);
        consoleWrite("             Joshtech @GBATemp.net",false);
        consoleWrite("==============================", false);
        consoleWrite("- Buffer is set to " + Integer.toString(bufferSize) + "kb", true);
        
        getCiaListing();
        resetControls();
    }
    
    
    
    public void GetSettings(){
        File tempFile = new File(CONST_SETTING_PATH);
        // if file doesn't exists, then create it
        if (!tempFile.exists()) {
        	consoleWrite("- Settings file could not be found. Creating default.", true);
                WriteTextFile(CONST_SETTING_PATH, "// This file contains settings for SocketPunch" + NEWLINE + "// Please keep in mind that buffersize should match with FBI (default 128)" + NEWLINE + "savelog=true" + NEWLINE + "buffersize=128");
                GetSettings();
        } else {
            // Open settings file and split it by lines
            String[] file = ReadTextFile(CONST_SETTING_PATH).split(NEWLINE);
            // For each line
            for (String fileLine : file) {
                // If it's not a comment
                if (!fileLine.contains("//")) {
                    try {
                        String[] tempSplit = fileLine.split("=");
                        switch(tempSplit[0].toLowerCase()){
                            case "savelog":
                                saveLogChk.setSelected(Boolean.valueOf(tempSplit[1]));
                                break;
                            case "buffersize":
                                bufferSize = Integer.parseInt(tempSplit[1]);
                                break;
                            case "ip":
                                ipText.setText(tempSplit[1]);
                        }
                    }catch (Exception e) {
                        consoleWrite("- There was an issue loading settings!", true);
                    }
                }
            }
        }
    }
    
    public void OpenDir(String path){
        File f = new File(path);
        File[] list = f.listFiles();
        for (File file : list){
            try {
                // Check if it's a folder!
                if(file.isDirectory()){
                    OpenDir(file.toString());
                // Guess it's a cia
                } else if (file.isFile() && file.toString().toLowerCase().contains(".cia")){
                    // Add that shit to the model mate! 
                    model.addElement(file.toString());
                }
            } catch (Exception e) {
            }
        }
    }
    
    public String ReadTextFile(String path){
        String tempString = "";
        try {
            Reader file = new FileReader(path);
            int data = file.read();
            while(data != -1){
                tempString = tempString + (char) data;
                data = file.read();
            }
            file.close();
        } catch (Exception e) {
            return "";
        }
        return tempString;
    }
    
    public void WriteTextFile(String path, String text){
        BufferedWriter bw = null;
        FileWriter fw;
         try {
            File file = new File(path);
            // if file doesnt exists, then create it
            if (!file.exists()) {
        	file.createNewFile();
            }
            fw = new FileWriter(file.getAbsoluteFile(), true);
            bw = new BufferedWriter(fw);
            bw.write(text);
            bw.newLine();
            bw.flush();
 	} catch (IOException e) {
            consoleWrite("- Unable to write file. Weird!", true);
	} finally { // always close the file
            if (bw != null) try {
                bw.close();
            } catch (IOException e) {
                        // just ignore it
            }
        }
    }
    
    
     public void WriteLog(String path, String text){
        WriteTextFile(path, text);
    }
    
    public void getCiaListing(){
        consoleWrite("- Getting directory listings.", true);
        OpenDir(System.getProperty("user.dir"));
        if(model.getSize() == 0){
            model.addElement("NO CIA'S FOUND!");
        }
        queueList.setModel(queueModel);
        queueList.setSelectedIndex(0);
        dirList.setModel(model);
        dirList.setSelectedIndex(0);
    }
    public void resetControls(){
        progBar.setValue(0);
        transferCount = 0;
        progText.setText(String.format("%.2fmb of %.2fmb @ %%%.2f",0f, 0f,0f));
    }
    
    // Simple method to add text to console box
    public void consoleWrite(String string, boolean withTime){
        String tempString;
        if(withTime){
            tempString = consoleText.getText() + getTimeStamp() + " " + string + NEWLINE;
            if(saveLogChk.isSelected()){
                WriteLog(CONST_PATH, getTimeStamp() + " " + string);
            }
            consoleText.setText(tempString);
        } else {
            tempString = consoleText.getText() + string + NEWLINE;
            if(saveLogChk.isSelected()){
                WriteLog(CONST_PATH, string);
            }
            consoleText.setText(tempString);
        }
        consoleText.setCaretPosition(consoleText.getDocument().getLength());
    }
    public String getTimeStamp(){
        Calendar cal = Calendar.getInstance();
    	cal.getTime();
    	SimpleDateFormat sdf = new SimpleDateFormat("[d/M/y HH:mm a]");
        return sdf.format(cal.getTime());
    }
    public boolean connect(File file){
		Socket socket;
		DataOutputStream out;
                // Create socket
                try {
			socket = new Socket(ipText.getText(), 5000);
			out = new DataOutputStream(socket.getOutputStream());
		} catch(IOException e) {
                        // Only get 1 failed to open socket per file, else just say retrying
                        if(failCount == 0){
                            consoleWrite("- Failed to open socket. Please check IP.", true);
                        } else {
                            consoleWrite("- Retrying!", true);
                        }
			
			resetControls();
			return false;
		}
                // Open file for streaming
		FileInputStream in;
		try {
			in = new FileInputStream(file);
		} catch(IOException e) {
			consoleWrite("- Failed to open file stream.", true);
                        resetControls();
			return false;
		}
                // Send file
                long counter = 0;
                int updateCount = 0;
		try {
			consoleWrite("- Sending info...", true);
			out.writeLong(file.length());
                        progBar.setMaximum((int) file.length());
			consoleWrite(String.format("- Sending file with size of %.2fmb",(float) file.length() / 1048576), true);
			byte buffer[] = new byte[1024 * bufferSize];
			int length;
			while((length = in.read(buffer)) != -1) {
				out.write(buffer, 0, length);
                                counter = counter + length;
                                // Left this in just incase
                                if(updateCount < 0){
                                    updateCount++;
                                } else {
                                    updateCount = 0;
                                    progBar.setValue((int) counter);
                                    progText.setText(String.format("%.2fmb of %.2fmb @ %.2f%%",(float) counter / 1048576, (float) file.length() / 1048576, (((float) counter / 1048576) / ((float) file.length() / 1048576))* 100));
                            }
                        }
                        resetControls();
			consoleWrite("- File sent successfully.", true);
                        return true;
		} catch(IOException e) {
                        resetControls();
			consoleWrite(String.format("- Failed at %.2fmb of %.2fmb @ %.2f%%",(float) counter / 1048576, (float) file.length() / 1048576, (((float) counter / 1048576) / ((float) file.length() / 1048576))* 100), true);
                        return false;
		} finally {
			try {
				in.close();
				out.close();
				socket.close();
			} catch(IOException e) {
			}
		}

    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        progBar = new javax.swing.JProgressBar();
        jScrollPane1 = new javax.swing.JScrollPane();
        queueList = new javax.swing.JList();
        jScrollPane3 = new javax.swing.JScrollPane();
        ipText = new javax.swing.JTextPane();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        consoleText = new javax.swing.JTextArea();
        jLabel3 = new javax.swing.JLabel();
        startButt = new javax.swing.JButton();
        progText = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        dirList = new javax.swing.JList();
        jLabel4 = new javax.swing.JLabel();
        autotryChk = new javax.swing.JCheckBox();
        timeoutVal = new javax.swing.JSpinner();
        jLabel5 = new javax.swing.JLabel();
        saveLogChk = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        queueList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                queueListMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(queueList);

        ipText.setText("Enter IP Address");
        ipText.setToolTipText("");
        ipText.setName(""); // NOI18N
        jScrollPane3.setViewportView(ipText);

        jLabel1.setText("IP Address:   (This should be shown on device)");

        jLabel2.setText("CIA List:   (Double-Click to add to queue)");

        consoleText.setColumns(20);
        consoleText.setFont(new java.awt.Font("Malgun Gothic", 0, 10)); // NOI18N
        consoleText.setRows(5);
        jScrollPane2.setViewportView(consoleText);

        jLabel3.setText("Console Output:");

        startButt.setText("Punch!");
        startButt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startButtActionPerformed(evt);
            }
        });

        progText.setText(" ");

        dirList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                dirListMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(dirList);

        jLabel4.setText("CIA's in queue:   (Double-Click to remove from queue)");

        autotryChk.setText("Auto-Retry");

        timeoutVal.setValue(5);

        jLabel5.setText("Timeout Tries:");

        saveLogChk.setText("Save Log (Will be stored in working directory)");
        saveLogChk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveLogChkActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 548, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(progBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane3)
                    .addComponent(startButt, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane4)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(saveLogChk))
                            .addComponent(jLabel4))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(progText)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(timeoutVal, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(autotryChk)
                        .addGap(25, 25, 25)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(saveLogChk))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(startButt)
                        .addGap(18, 18, 18)
                        .addComponent(progText))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(autotryChk)
                        .addComponent(timeoutVal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel5)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(progBar, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void startButtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startButtActionPerformed
        Thread thread = new Thread("Connect") {
        
        public void run(){
            if(!startedSending){
                // Set flag to true so we don't get multiple connects
                startedSending = true;
                failCount = 0;
                boolean connectStatus;
                // For each file in queue
                for (int i = 0; i < queueList.getModel().getSize(); i++) {
                    Object item = queueList.getModel().getElementAt(i);
                    queueList.setSelectedIndex(i);
                    connectStatus = connect(new File(item.toString()));
                    if(!connectStatus && autotryChk.isSelected()){
                        i--;
                    }else if(!connectStatus && failCount < (Integer)timeoutVal.getValue() - 1){
                        // Lazy way to retry file, but since there is no real way
                        // to tell why it errored we will try set times each file
                        i--;
                        failCount++;
                    } else {
                        failCount = 0;
                    }
                }
                // Set flag false so a new send queue can be init
                startedSending = false;
                consoleWrite("- Queue has finished!", true);
            } else {
                consoleWrite("- A file transfer is already in progress.", true);
            }
        }
   };
   thread.start();
    }//GEN-LAST:event_startButtActionPerformed

    private void dirListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dirListMouseClicked
        if (evt.getClickCount() == 2) {
            // Double-click detected
            queueModel.addElement(dirList.getSelectedValue());
        } 
    }//GEN-LAST:event_dirListMouseClicked

    private void queueListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_queueListMouseClicked
        if (evt.getClickCount() == 2) {
            // Double-click detected
            queueModel.removeElement(queueList.getSelectedValue());
        }
    }//GEN-LAST:event_queueListMouseClicked

    private void saveLogChkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveLogChkActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_saveLogChkActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        /*try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }*/
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Main().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox autotryChk;
    private javax.swing.JTextArea consoleText;
    private javax.swing.JList dirList;
    private javax.swing.JTextPane ipText;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JProgressBar progBar;
    private javax.swing.JLabel progText;
    private javax.swing.JList queueList;
    private javax.swing.JCheckBox saveLogChk;
    private javax.swing.JButton startButt;
    private javax.swing.JSpinner timeoutVal;
    // End of variables declaration//GEN-END:variables
}
