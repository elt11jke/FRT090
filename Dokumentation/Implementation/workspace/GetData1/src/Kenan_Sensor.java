//import lejos.hardware.port.I2CException;
//
//
//public class Kenan_Sensor {
//	
//	
//	
//
//	
//	   /**
//     * High level i2c interface. Perform a complete i2c transaction and return
//     * the results. Writes the specified data to the device and then reads the
//     * requested bytes from it.
//     * @param deviceAddress The I2C device address.
//     * @param writeBuf The buffer containing data to be written to the device.
//     * @param writeOffset The offset of the data within the write buffer
//     * @param writeLen The number of bytes to write.
//     * @param readBuf The buffer to use for the transaction results
//     * @param readOffset Location to write the results to
//     * @param readLen The length of the read
//     */
//	
//	
//	private final int deviceAddress = 0x02;
//	private final byte[]writeBuf=new byte[32] ;
//	private final int writeOffset = 0;
//	private final int writeLen= 1;
//	private final byte[] readBuf = ;
//	int readOffset=;
//	int readLen;
//   
//    {
//        long timeout = System.currentTimeMillis() + IO_TIMEOUT;
//        //System.out.println("ioctl: " + deviceAddress + " wlen " + writeLen);
//        iicdata.Port = (byte)port;
//        iicdata.Result = -1;
//        iicdata.Repeat = 1;
//        iicdata.Time = 0;
//        iicdata.WrLng = (byte)(writeLen + 1);
//        System.arraycopy(writeBuf, writeOffset, iicdata.WrData, 1, writeLen);
//        iicdata.WrData[0] = (byte)(deviceAddress >> 1);
//        iicdata.WrLng = (byte)(writeLen + 1);
//        // note -ve value due to Lego's crazy reverse order stuff
//        iicdata.RdLng = (byte)-readLen;
//        iicdata.write();
//        while(timeout > System.currentTimeMillis())
//        {
//            iicdata.write();
//            i2c.ioctl(IIC_SETUP, iicdata.getPointer());
//            iicdata.read();
//            //System.out.println("Ioctl result: " + iicdata.Result);
//            if (iicdata.Result < 0)
//                throw new I2CException("I2C read error");
//            if (iicdata.Result == STATUS_OK)
//            {
//                if (readLen > 0)
//                    System.arraycopy(iicdata.RdData, 0, readBuf, readOffset,  readLen);
//                return;
//            }
//            Thread.yield();
//        }
//        throw new I2CException("I2C timeout");
//    }
//    
//
//}