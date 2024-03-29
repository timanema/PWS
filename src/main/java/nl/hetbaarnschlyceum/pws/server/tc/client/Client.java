package nl.hetbaarnschlyceum.pws.server.tc.client;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.security.KeyPair;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class Client {
    private String name;                            // Naam van de gebruiker
    private boolean publicCL;                       // Boolean die aangeeft of een gebruiker te vinden is
    private int number;                             // Nummer van de gebruiker
    private Blob RSAKey;                            // Publieke RSA sleutel van de gebruiker
    private boolean hidden;                         // Boolean die aangeeft of de gebruiker een whitelist heeft
    private int[] whitelist;                        // Array met nummers die op de whitelist staan
    private UUID uuid;                              // Universally unique identifier van de gebruiker
    private int status;                             // Status van de gebruiker die aangeeft of de gebruiker online is
    private SecretKey sessionKey;                   // De AES-sleutel voor de huidige sessie
    private IvParameterSpec initializationVector;   // De AES-IV
    private String hmacKey;
    private ArrayList<String> failedAttempts;
    private String IP;
    private KeyPair dhKeys;
    private long lastMessageReceived;

    private SocketChannel socketChannel;
    public ByteBuffer byteBuffer;

    private HashMap<UUID, String> requestList;
    private ArrayList<UUID> awaitingRequests;

    public Client(SocketChannel socketChannel, String IP)
    {
        this.socketChannel = socketChannel;
        this.byteBuffer = ByteBuffer.allocate(8192);
        this.uuid = UUID.randomUUID();
        this.failedAttempts = new ArrayList<>();
        this.IP = IP;
        this.requestList = new HashMap<>();
        this.awaitingRequests = new ArrayList<>();
    }

    public String getIP()
    {
        return this.IP;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public boolean isPublic()
    {
        return publicCL;
    }

    public void setPublic(boolean public_cl)
    {
        this.publicCL = public_cl;
    }

    public int getNumber()
    {
        return number;
    }

    public void setNumber(int number)
    {
        this.number = number;
    }

    public Blob getRSAKey()
    {
        return RSAKey;
    }

    public void setRSAKey(Blob RSAKey)
    {
        this.RSAKey = RSAKey;
    }

    public boolean isHidden()
    {
        return hidden;
    }

    public void setHidden(boolean hidden)
    {
        this.hidden = hidden;
    }

    public int[] getWhitelist()
    {
        return whitelist;
    }

    public void setWhitelist(int[] whitelist)
    {
        this.whitelist = whitelist;
    }

    public UUID getUUID()
    {
        return uuid;
    }

    public void setUUID(UUID uuid)
    {
        this.uuid = uuid;
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public String getHMACKey()
    {
        return hmacKey;
    }

    public void setHMACKey(String hmacKey)
    {
        this.hmacKey = hmacKey;
    }

    public SecretKey getSessionKey()
    {
        return sessionKey;
    }

    public void setSessionKey(SecretKey session_key)
    {
        this.sessionKey = session_key;
    }

    public IvParameterSpec getInitializationVector()
    {
        return initializationVector;
    }

    public void setInitializationVector(IvParameterSpec initializationVector)
    {
        this.initializationVector = initializationVector;
    }

    public SocketChannel getSocketChannel()
    {
        return this.socketChannel;
    }

    public void setDHKeys(KeyPair keys)
    {
        this.dhKeys = keys;
    }

    public KeyPair getDHKeys()
    {
        return dhKeys;
    }

    public void setLastMessageReceivedTime(long start)
    {
        this.lastMessageReceived = start;
    }

    public long getLastMessageReceivedTime()
    {
        return this.lastMessageReceived;
    }

    public void addRequestResult(UUID requestID, String result)
    {
        awaitingRequests.remove(uuid);
        requestList.put(requestID, result);
    }

    public HashMap<UUID, String> getResultList()
    {
        return this.requestList;
    }

    public void addAwaitingRequest(UUID uuid)
    {
        awaitingRequests.add(uuid);
    }

    public boolean isWaiting(UUID uuid)
    {
        return awaitingRequests.contains(uuid);
    }

    public void addFailedAttempt()
    {
        this.failedAttempts.add(String.format("[%1$s] Inlogpoging vanaf %2$s met verkeerd wachtwoord",
                new Timestamp(System.currentTimeMillis()).toString(),
                IP));
    }

    public String toString()
    {
        return String.format("name=%s, public_cl=%b, number=%d, hidden=%b, uuid=%s, status=%d",
                name,
                publicCL,
                number,
                hidden,
                uuid.toString(),
                status);
    }
}
