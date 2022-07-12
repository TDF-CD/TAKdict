
using System.Security.Cryptography;
using System.Security.Cryptography.X509Certificates;
using System.Security.Cryptography.Xml;
using System.Text.RegularExpressions;
using System.Xml;
 
public class XAdES
{
    private static readonly Regex pattern = new Regex("_(.+?)_", RegexOptions.Compiled);
 
    private X509Certificate2 _cert;
    private SignedXml _xml;
 
    public byte[] Content
    {
        get
        {
            if (_xml == null)
            {
                throw new NullReferenceException("Xml not loaded");
            }
 
            var id = _xml.Signature.Id;
            var match = pattern.Match(id);
            if (!match.Success)
            {
                throw new FormatException("Signature id");
            }
 
            var objId = match.Groups[1].Value;
 
            foreach (DataObject dataObject in _xml.Signature.ObjectList)
            {
                if (dataObject.Id == null ||
                    !dataObject.Id.Contains(objId))
                {
                    continue;
                }
 
                return Convert.FromBase64String(dataObject.Data.Item(0).InnerText);
            }
 
            throw new FormatException("No objects embedded");
        }
    }
 
    public X509Certificate2 Certificate
    {
        get { return _cert; }
    }
 
    // loads signed xml document for public use
    public void LoadXml(string xml)
    {

        XmlDocument xmlDoc = new XmlDocument();
        // Load an XML file into the XmlDocument object.
        xmlDoc.PreserveWhitespace = true;
        xmlDoc.Load(xml);
        Load(xmlDoc);
        
    }
 
    // loads signed xml document for private use
    private void Load(XmlDocument doc)
    {
        if (doc.DocumentElement == null)
        {
            throw new NullReferenceException("Document root");
        }
 
        _cert = ExtractCertificate(doc);
 
        _xml = new SignedXml(doc);
        _xml.LoadXml(doc.DocumentElement);
    }
 
    // extracting cert from xml document
    private X509Certificate2 ExtractCertificate(XmlDocument doc)
    {
        var namespaceManager = new XmlNamespaceManager(doc.NameTable);
        namespaceManager.AddNamespace("ds", "http://www.w3.org/2000/09/xmldsig#");
        var node = doc.SelectSingleNode("/ds:Signature/ds:KeyInfo/ds:X509Data/ds:X509Certificate", namespaceManager);
        if (node == null)
        {
            throw new FormatException("No certificate");
        }
 
        return new X509Certificate2(Convert.FromBase64String(node.InnerText));
    }
 
    // verifies xades signature
    public bool Verify(bool verifySIgnatureOnly = false)
    {
        return _xml.CheckSignature(_cert, verifySIgnatureOnly);
    }
 

    // checks if given cert has valid chain up to root cert
    // certs and revocation lists must be installed in operating system
    public void CheckRoot(string thumbprint)
    {
        var chain = new X509Chain();
        // to debug import of revocation list uncomment next line
        //chain.ChainPolicy.RevocationMode = X509RevocationMode.NoCheck;
        var result = chain.Build(Certificate);
        if (!result)
        {
            throw new Exception("Unable to build certificate chain");
        }
 
        if (chain.ChainElements.Count == 0)
        {
            throw new Exception("Certificate chain length is 0");
        }
 
        if (
            StringComparer.OrdinalIgnoreCase.Compare(chain.ChainElements[chain.ChainElements.Count - 1].Certificate.Thumbprint,
                                                     thumbprint) != 0)
        {
            throw new Exception("Root certificate thumbprint mismatch");
        }

    }
}