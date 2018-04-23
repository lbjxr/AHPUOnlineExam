package exam.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import exam.model.role.Student;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import exam.dto.StudentReport;

/**
 * 生成excel表格
 * 导入excel表格
 * @author skywalker
 *
 */
public abstract class ExcelUtil {

	/**
	 * 生成试卷成绩单
	 * @param reportData 数据
	 * @param path 保存的路径
	 * @throws IOException
	 * @return InputStream 返回生成的文件的输入流
	 */
	public static InputStream generateExcel(List<StudentReport> reportData, String path) throws IOException {
		File file = new File(path);
		if (file.exists()) {
			return new FileInputStream(file);
		}
		HSSFWorkbook wb = new HSSFWorkbook();
		//创建一个sheet
		HSSFSheet sheet = wb.createSheet(reportData.get(0).getTitle() + "成绩单");
		//添加表头(第0行)
		HSSFRow row = sheet.createRow(0);
		//设置表头居中
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		//设置第一行数据(表头)
		HSSFCell cell = row.createCell(0);
		cell.setCellValue("学号");
		cell.setCellStyle(style);
		cell = row.createCell(1);
		cell.setCellValue("姓名");
		cell.setCellStyle(style);
		cell = row.createCell(2);
		cell.setCellValue("分数");
		cell.setCellStyle(style);
		//写入数据
		StudentReport data = null;
		for (int i = 0, l = reportData.size(); i < l;i ++) {
			data = reportData.get(0);
			row = sheet.createRow(i + 1);
			row.createCell(0).setCellValue(data.getSid());
			row.createCell(1).setCellValue(data.getName());
			row.createCell(2).setCellValue(data.getPoint());
		}
		//保存
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		FileOutputStream fos = new FileOutputStream(file);
		wb.write(baos);
		wb.write(fos);
		wb.close();
		return new ByteArrayInputStream(baos.toByteArray());
	}


	/**
	 * 批量导入学生信息
	 * @return
	 * @throws IOException
	 */
	public List<Student> readStudentXls() throws IOException {
		InputStream is = new FileInputStream("excel/Students.xls");
		HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
		Student student = null;
		List<Student> list = new ArrayList<Student>();
		// 循环工作表Sheet
		for (int numSheet = 0; numSheet < hssfWorkbook.getNumberOfSheets(); numSheet++) {
			HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
			if (hssfSheet == null) {
				continue;
			}
			// 循环行Row
			for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
				HSSFRow hssfRow = hssfSheet.getRow(rowNum);
				if (hssfRow != null) {
					student = new Student();
					HSSFCell id = hssfRow.getCell(0);
					HSSFCell name = hssfRow.getCell(1);
					HSSFCell password = hssfRow.getCell(2);
					HSSFCell classid = hssfRow.getCell(3);
					HSSFCell modified = hssfRow.getCell(4);
					student.setId(getValue(id));
					student.setName(getValue(name));
//					student.setClazz(getValue(classid));
					student.setModified(Integer.parseInt(getValue(modified)) == 0 ? false:true);
					list.add(student);
				}
			}
		}
		return list;
	}

	@SuppressWarnings("static-access")
	private String getValue(HSSFCell hssfCell) {
		if (hssfCell.getCellType() == hssfCell.CELL_TYPE_BOOLEAN) {
			// 返回布尔类型的值
			return String.valueOf(hssfCell.getBooleanCellValue());
		} else if (hssfCell.getCellType() == hssfCell.CELL_TYPE_NUMERIC) {
			// 返回数值类型的值
			return String.valueOf(hssfCell.getNumericCellValue());
		} else {
			// 返回字符串类型的值
			return String.valueOf(hssfCell.getStringCellValue());
		}
	}
}
