using System.Security.Cryptography;
using PgpCore;


namespace Integrity;
class Integrity{

    private XAdES xades = new XAdES();


    // verifies pgp signature of file with given filename with public key
    public bool verifyPgp(String file_name, String pub_key_filename){
        FileInfo pub_key = new FileInfo(pub_key_filename);
        EncryptionKeys encryptionKeys = new EncryptionKeys(pub_key);
        FileInfo input_file = new FileInfo(file_name);
        PGP pgp = new PGP(encryptionKeys);
        return pgp.VerifyClearFile(input_file);
    }

    // returns Base64 encoded SHA1 digest of file with given file name
    public String getHash(String file_name){

        SHA1Managed sha = new SHA1Managed();
        using (sha){
            using (var file_stream = new FileStream(file_name, 
                                               FileMode.Open, 
                                               FileAccess.Read, 
                                               FileShare.ReadWrite))
            {
                var hash = sha.ComputeHash(file_stream);
                var hash_string = Convert.ToBase64String(hash);
                return hash_string.TrimEnd('=');
            }
        }
    }    

    // todo
    public String search(String text, String file_name, int b){
        const string dataFolder = "Data"; //todo
        String end_filename = "APP6B_to_CoT.csv";
        String line = null;
        
        //Selecting file,  default is APPd6B_to_CoT.csv
        if(String.Compare(file_name,"CoT_to_APP6B.csv") == 0){
            end_filename = "CoT_to_APP6B.csv";
        }
        try{
        StreamReader read_file = new StreamReader(end_filename);
        read_file.ReadLine();
        while ((line = read_file.ReadLine()) != null)
        {
            if(String.Compare(text,line.Split(";")[0]) == 0) break;
        }
        read_file.Close();
        }catch(Exception e){
            Console.WriteLine("A reading file problem.\n"+e.Message);
            Environment.Exit(1);
        }
        if(line == null) throw new Exception("File not found in .csv");
        return(line.Split(";")[2]);
    }


    // verifies xades signature of given xades(xml) file
    public bool verifyXades(string xml_path){
        
        xades.LoadXml(xml_path);
        return xades.Verify();
    }


    // checks cert chain up to root certificate
    public void checkCertRoot(String thumb_print){
        xades.CheckRoot(thumb_print);
    }
}

