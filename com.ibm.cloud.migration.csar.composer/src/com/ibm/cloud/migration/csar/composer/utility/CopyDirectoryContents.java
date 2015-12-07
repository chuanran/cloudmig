package com.ibm.cloud.migration.csar.composer.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class CopyDirectoryContents {  
  
    private static String MESSAGE = "";  
  
    /** 
     * ���Ƶ����ļ� 
     *  
     * @param srcFileName 
     *            �����Ƶ��ļ��� 
     * @param descFileName 
     *            Ŀ���ļ��� 
     * @param overlay 
     *            ���Ŀ���ļ����ڣ��Ƿ񸲸� 
     * @return ������Ƴɹ�����true�����򷵻�false 
     */  
    public static boolean copyFile(String srcFileName, String destFileName,  
            boolean overlay) {  
        File srcFile = new File(srcFileName);  
  
        // �ж�Դ�ļ��Ƿ����  
        if (!srcFile.exists()) {  
            MESSAGE = "Դ�ļ���" + srcFileName + "�����ڣ�";  
            return false;  
        } else if (!srcFile.isFile()) {  
            MESSAGE = "�����ļ�ʧ�ܣ�Դ�ļ���" + srcFileName + "����һ���ļ���";  
            return false;  
        }  
  
        // �ж�Ŀ���ļ��Ƿ����  
        File destFile = new File(destFileName);  
        if (destFile.exists()) {  
            // ���Ŀ���ļ����ڲ�������  
            if (overlay) {  
                // ɾ���Ѿ����ڵ�Ŀ���ļ�������Ŀ���ļ���Ŀ¼���ǵ����ļ�  
                new File(destFileName).delete();  
            }  
        } else {  
            // ���Ŀ���ļ�����Ŀ¼�����ڣ��򴴽�Ŀ¼  
            if (!destFile.getParentFile().exists()) {  
                // Ŀ���ļ�����Ŀ¼������  
                if (!destFile.getParentFile().mkdirs()) {  
                    // �����ļ�ʧ�ܣ�����Ŀ���ļ�����Ŀ¼ʧ��  
                    return false;  
                }  
            }  
        }  
  
        // �����ļ�  
        int byteread = 0; // ��ȡ���ֽ���  
        InputStream in = null;  
        OutputStream out = null;  
  
        try {  
            in = new FileInputStream(srcFile);  
            out = new FileOutputStream(destFile);  
            byte[] buffer = new byte[1024];  
  
            while ((byteread = in.read(buffer)) != -1) {  
                out.write(buffer, 0, byteread);  
            }  
            return true;  
        } catch (FileNotFoundException e) {  
            return false;  
        } catch (IOException e) {  
            return false;  
        } finally {  
            try {  
                if (out != null)  
                    out.close();  
                if (in != null)  
                    in.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
    }  
  
    /** 
     * ��������Ŀ¼������ 
     *  
     * @param srcDirName 
     *            ������Ŀ¼��Ŀ¼�� 
     * @param destDirName 
     *            Ŀ��Ŀ¼�� 
     * @param overlay 
     *            ���Ŀ��Ŀ¼���ڣ��Ƿ񸲸� 
     * @return ������Ƴɹ�����true�����򷵻�false 
     */  
    public static boolean copyDirectory(String srcDirName, String destDirName,  
            boolean overlay) {  
        // �ж�ԴĿ¼�Ƿ����  
        File srcDir = new File(srcDirName);  
        if (!srcDir.exists()) {  
            MESSAGE = "����Ŀ¼ʧ�ܣ�ԴĿ¼" + srcDirName + "�����ڣ�";  
            return false;  
        } else if (!srcDir.isDirectory()) {  
            MESSAGE = "����Ŀ¼ʧ�ܣ�" + srcDirName + "����Ŀ¼��";  
            return false;  
        }  
  
        // ���Ŀ��Ŀ¼���������ļ��ָ�����β��������ļ��ָ���  
        if (!destDirName.endsWith(File.separator)) {  
            destDirName = destDirName + File.separator;  
        }  
        File destDir = new File(destDirName);  
        // ���Ŀ���ļ��д���  
        if (destDir.exists()) {  
            // �����������ɾ���Ѵ��ڵ�Ŀ��Ŀ¼  
            if (overlay) {  
                new File(destDirName).delete();  
            } else {  
                MESSAGE = "����Ŀ¼ʧ�ܣ�Ŀ��Ŀ¼" + destDirName + "�Ѵ��ڣ�";  
                return false;  
            }  
        } else {  
            // ����Ŀ��Ŀ¼  
            System.out.println("Ŀ��Ŀ¼�����ڣ�׼������������");  
            if (!destDir.mkdirs()) {  
                System.out.println("����Ŀ¼ʧ�ܣ�����Ŀ��Ŀ¼ʧ�ܣ�");  
                return false;  
            }  
        }  
  
        boolean flag = true;  
        File[] files = srcDir.listFiles();  
        for (int i = 0; i < files.length; i++) {  
            // �����ļ�  
            if (files[i].isFile()) {  
                flag = CopyDirectoryContents.copyFile(files[i].getAbsolutePath(),  
                        destDirName + files[i].getName(), overlay);  
                if (!flag)  
                    break;  
            } else if (files[i].isDirectory()) {  
                flag = CopyDirectoryContents.copyDirectory(files[i].getAbsolutePath(),  
                        destDirName + files[i].getName(), overlay);  
                if (!flag)  
                    break;  
            }  
        }  
        if (!flag) {  
            MESSAGE = "����Ŀ¼" + srcDirName + "��" + destDirName + "ʧ�ܣ�";  
            return false;  
        } else {  
            return true;  
        }  
    }  
  
//    public static void main(String[] args) {  
//    	String cp = CopyDirectoryContents.class.getResource("").getPath();
//    	
//        String srcDirName = cp + "test";  
//        System.out.println(srcDirName);
//        String destDirName = cp + "hello";  
//        CopyDirectoryContents.copyDirectory(srcDirName, destDirName, true);  
//    }  
}  
    	


