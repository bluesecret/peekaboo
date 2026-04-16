package io.wangk.peekaboo.common.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;

//TODO 删除
@Deprecated
public class FileUtil {

    public static String saveFile(MultipartFile file, String path) {
        try {
            String fileName = file.getOriginalFilename();
            String newPath = (path + "/" + fileName.substring(0, fileName.lastIndexOf(".")) + "_"
                    + System.currentTimeMillis() + "_" + (int) ((Math.random() * 9 + 1) * 100) +
                    fileName.substring(fileName.lastIndexOf("."), fileName.length()))
                    .replaceAll("/+", "/");

            File tempFile = new File(newPath);

            if (!tempFile.exists()) {
                tempFile.getParentFile().mkdirs();
            }

            file.transferTo(tempFile); // 保存文件
//            FileUtils.copyInputStreamToFile(file.getInputStream(), tempFile);
            System.out.println(tempFile.getPath());
            return tempFile.getPath();
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean deletefile(File file) {
        if (!file.exists()) {
            return false;
        }

        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                deletefile(f);
            }
        }
        return file.delete();
    }

    public static String savebdcyxFile(MultipartFile file, String filesavapath, String ywTradeNo, String ImgType,
                                       String nowTime) {
        try {
            String fileName = file.getOriginalFilename();
            System.out.println(fileName);
            File tempFile = new File(filesavapath + "/" + ywTradeNo + "/" + ImgType + "/" + nowTime);
            if (tempFile.exists()) {
                deletefile(tempFile);
            }
            tempFile.mkdirs();
            File dest = new File(tempFile.getPath() + File.separator + fileName);

            file.transferTo(dest); // 保存文件
            return dest.getPath();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 根据byte数组，生成文件
     * @param data 文件数组
     * @param filePath 文件存放路径
     * @param fileName 文件名称
     */
    public static String byte2File(byte[] data,String filePath,String fileName){
//        String newPath = (filePath + "/" + fileName.substring(0, fileName.lastIndexOf(".")) + "_"
//                + System.currentTimeMillis() + "_" + (int) ((Math.random() * 9 + 1) * 100) + fileName.substring(fileName.lastIndexOf("."), fileName.length()))
//                .replaceAll("/+", "/");
        BufferedOutputStream bos=null;
        FileOutputStream fos=null;
        File file=null;
        try{
 //           File dir=new File(filePath);
//            if(!dir.exists() && !dir.isDirectory()){//判断文件目录是否存在
//                dir.mkdirs();
//            }

            file=new File(filePath+File.separator+fileName);
            if (!file.exists()) {
                file.getParentFile().mkdirs();
            }
            fos=new FileOutputStream(file);
            bos=new BufferedOutputStream(fos);
            bos.write(data);
            return file.getPath();
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        finally{
            try{
                if(bos != null){
                    bos.close();
                }
                if(fos != null){
                    fos.close();
                }
            }
            catch(Exception e){
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }
        return null;
    }
    /**
     * 获得指定文件的byte数组
     * @param filePath 文件绝对路径
     * @return
     */
    public static byte[] file2Byte(String filePath){
        ByteArrayOutputStream bos=null;
        BufferedInputStream in=null;
        try{
            File file=new File(filePath);
            if(!file.exists()){
                throw new FileNotFoundException("file not exists");
            }
            bos=new ByteArrayOutputStream((int)file.length());
            in=new BufferedInputStream(new FileInputStream(file));
            int buf_size=1024;
            byte[] buffer=new byte[buf_size];
            int len=0;
            while(-1 != (len=in.read(buffer,0,buf_size))){
                bos.write(buffer,0,len);
            }
            return bos.toByteArray();
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
            return null;
        }
        finally{
            try{
                if(in!=null){
                    in.close();
                }
                if(bos!=null){
                    bos.close();
                }
            }
            catch(Exception e){
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }
    }
    //TODO 20231207修改点
    public static String saveFileNewName(MultipartFile file, String path,String id) {
        try {
            String fileName = file.getOriginalFilename();
            String newPath = (path + "/" +id+"_checkfile"+
                    fileName.substring(fileName.lastIndexOf("."), fileName.length()))
                    .replaceAll("/+", "/");

            File tempFile = new File(newPath);

            if (!tempFile.exists()) {
                tempFile.getParentFile().mkdirs();
            }

            file.transferTo(tempFile); // 保存文件
//            FileUtils.copyInputStreamToFile(file.getInputStream(), tempFile);
            System.out.println(tempFile.getPath());
            return tempFile.getPath();
        } catch (Exception e) {
            return null;
        }
    }

    public static void main(String[] args) {
        try {
            byte[] data=file2Byte("D:\\Tools\\upFiles\\委托书.pdf");
            System.out.println(byte2File(data,"D:\\Tools\\upFiles","20211119_1212.pdf"));
        }catch (Exception e){

        }
    }
}
