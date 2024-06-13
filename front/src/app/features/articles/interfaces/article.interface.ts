export interface Article {
    id: number;
    title: string;
    content: string;
    user_id: number;
    topic_id: number;
    comments: number[];
    createdAt?: Date;
    updatedAt?: Date;
}