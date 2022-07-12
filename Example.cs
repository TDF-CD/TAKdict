using System.Security.Cryptography;
using PgpCore;

namespace Integrity;

class Example{
    static void Main(string[] args){

        var csv_file_path = "CoT_to_APP6B.csv";
        var pgp_file_path = "CoT_to_APP6B.csv.asc";
        var xades_file_path = "CoT_to_APP6B.csv.xades";
        var pub_key_filename = "rsa.asc";
        var root_thumbprint = "89cec4842faf401b48d0f21d8043e9a63e7c02d5";

        var t = new Integrity() ;
        var hash = t.getHash(csv_file_path);
        Console.WriteLine("Hash of " + csv_file_path + " : " + hash);

        var pgp_status = t.verifyPgp(pgp_file_path, pub_key_filename);
        Console.WriteLine("Status of pgp signature verification of  " + pgp_file_path + " : " + pgp_status);

        var xades_status = t.verifyXades(xades_file_path);
        Console.WriteLine("Status of xades signature verification of  " + xades_file_path + " : " + xades_status);
        
        t.checkCertRoot(root_thumbprint);

        try{
            String line = t.search("SFA-MHUH--","test",1);
            Console.WriteLine("Match: " + line);
        }catch(Exception e){
            Console.WriteLine(e.Message);
        }


    }
}
