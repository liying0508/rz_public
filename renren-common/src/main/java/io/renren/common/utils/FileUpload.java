package io.renren.common.utils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;


/**
 * 附件相关类
 * @author Lwy
 * @date 2018年12月19日16:20:42
 *
 */
public class FileUpload {
	
	/**
	 * 上传附件
	 * @param file 
	 * @return 服务器文件路径
	 */
	public static String writeFile(File file,String folderPath) {
		
		String savePath = generateFileSavePath(folderPath,file.getName());
		InputStream input=null;
		try {
			input = new FileInputStream(file);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try {
			OutputStream output = new FileOutputStream(folderPath+savePath);
			IOUtils.copy(input, output);
			output.close();
			input.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		return folderPath+savePath;
	}
	
	/**
	 * Web版生成附件的保存路径，不包含根路径。 文件的保存路径格式为：根 + 类型 + 年份 + 时间串 + 文件后缀
	 * @param rootPath
	 * @param origName 文件名
	 */
	private static String generateFileSavePath(String rootPath, String origName) {
		// 获取随机码
		String uuid = UUID.randomUUID().toString();
		int i = origName.lastIndexOf('.');
		String ext = i > -1 ? origName.substring(i) : ""; // 文件后缀名
		SimpleDateFormat format = new SimpleDateFormat("yyyyMM");
		String savePath =format.format(new Date())+ "\\"+uuid+ext;
		// 确保目录已被创建
		File file = new File(rootPath+savePath);
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		return savePath;
	}
	
	
	/**
	 * 下载或预览  路径方式
	 * @param filePath 
	 */
	public static void downLoad(String filePath, HttpServletResponse response, boolean isOnLine) throws Exception {
        File f = new File(filePath);
        if (!f.exists()) {
            response.sendError(404, "File not found!");
            return;
        }
        BufferedInputStream br = new BufferedInputStream(new FileInputStream(f));
        byte[] buf = new byte[1024];
        int len = 0;

        response.reset(); // 非常重要
        if (isOnLine) { // 在线打开方式
            URL u = new URL("file:///" + filePath);
            response.setContentType(u.openConnection().getContentType());
            response.setHeader("Content-Disposition", "inline; filename=" + f.getName());
            // 文件名应该编码成UTF-8
        } else { // 纯下载方式
            response.setContentType("application/x-msdownload");
            response.setHeader("Content-Disposition", "attachment; filename=" + f.getName());
        }
        OutputStream out = response.getOutputStream();
        while ((len = br.read(buf)) > 0){
            out.write(buf, 0, len);
        }
        br.close();
        out.close();
        
	 }
	
	/**
	 * 下载或预览 IO流
	 * @param filePath 
	 */
	public static void downLoadIO(String filePath, HttpServletResponse response) throws Exception{
		File f = new File(filePath);
	    if (!f.exists()) {
	        response.sendError(404, "File not found!");
	        return;
	    }
	    BufferedInputStream br = new BufferedInputStream(new FileInputStream(f));
        byte[] buf = new byte[1024];
        int len = 0;
        response.reset(); // 非常重要
        response.setContentType("application/x-msdownload");
        response.setHeader("Content-Disposition", "attachment; filename=" + f.getName());
        
        OutputStream out = response.getOutputStream();
        while ((len = br.read(buf)) > 0){
            out.write(buf, 0, len);
        }
        br.close();
        out.close();
	 }
	
	
	/**
	 * 返回文件流
	 * @param filePath
	 * @return
	 */
	public static ResponseEntity<byte[]> downloadByte(String filePath){
		File f = new File(filePath);
	    if (!f.exists()) {
	        return null;
	    }
		HttpHeaders headers = new HttpHeaders();
		try {
			headers.setContentDispositionFormData("attachment", f.getName());
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			
			return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(f),
					headers, HttpStatus.CREATED);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * 删除文件
	 * @param path
	 */
	public static boolean deleteFile(String path) {  
	    boolean flag = false;  
	    if(path!=null){
		    File file = new File(path);  
		    // 路径为文件且不为空则进行删除  
		    if (file.isFile() && file.exists()) {  
		        file.delete();  
		        flag = true;  
		    }  
	    }
	    return flag;  
	}  
	
	/**
     * 对临时生成的文件夹和文件夹下的文件进行删除
     */
    public static void deleteFileFolder(String delpath) {
        try {
            File file = new File(delpath);
            if (!file.isDirectory()) {
                file.delete();
            } else if (file.isDirectory()) {
                String[] filelist = file.list();
                for (int i = 0; i < filelist.length; i++) {
                    File delfile = new File(delpath + File.separator + filelist[i]);
                    if (!delfile.isDirectory()) {
                        delfile.delete();
                    } else if (delfile.isDirectory()) {
                    	deleteFileFolder(delpath + File.separator + filelist[i]);
                    }
                }
                file.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 根据图片地址获取二进制文件流
     * @param filePath
     * @return
     */
    public static byte[] getImageBinary(String filePath) {
		File f = new File(filePath);
		if (!f.exists()) {
	       throw new RuntimeException("附件不存在");
	    }
		BufferedImage bi;
		try {
			bi = ImageIO.read(f);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(bi, "jpg", baos);
			byte[] bytes = baos.toByteArray();
			return bytes;
			//return Base64.encodeBase64String(bytes);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
